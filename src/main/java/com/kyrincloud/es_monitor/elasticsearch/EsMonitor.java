package com.kyrincloud.es_monitor.elasticsearch;

import java.util.Iterator;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.joran.conditional.ElseAction;

import com.kyrincloud.es_monitor.Monitor;
import com.kyrincloud.es_monitor.config.ElasticsearchConfig;
import com.kyrincloud.es_monitor.config.PropertyConfigLoader;
import com.kyrincloud.es_monitor.queue.DataCache;
import com.kyrincloud.es_monitor.util.Util;

/**
 * 
 * @author kyrin `@date 2015年9月30日
 */
public final class EsMonitor implements Monitor{

	private static Logger logger = LoggerFactory.getLogger(EsMonitor.class);

	private  String host;
	private  int port ;
	private  String index ;
	private  String type ;
	private Client client;

	@SuppressWarnings("resource")
	public EsMonitor(ElasticsearchConfig config) {
		String _host = PropertyConfigLoader.getInstance().getProperties()
				.getProperty("elasticsearch.server");
		if (Util.validate("elasticsearch.server", _host)) {
			this.host=_host;
		}

		String _port = PropertyConfigLoader.getInstance().getProperties()
				.getProperty("elasticsearch.port");
		if (Util.validate("elasticsearch.port", _port)) {
			this.port=Integer.parseInt(_port);
		}

		String _index = PropertyConfigLoader.getInstance().getProperties()
				.getProperty("elasticsearch.index");
		if (Util.validate("elasticsearch.index", _index)) {
			this.index=_index;
		}

		String _type = PropertyConfigLoader.getInstance().getProperties()
				.getProperty("elasticsearch.type");
		if (Util.validate("elasticsearch.type", _type)) {
			this.type=_type;
		}

		Settings settings = ImmutableSettings
				.settingsBuilder()
				.put("cluster.name",
						PropertyConfigLoader.getInstance().getProperties()
								.getProperty("elasticsearch.cluster.name"))
				.build();
		client = new TransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(host,
						port));
	}

	public void start() {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				logger.info("elasticsearch服务检测开始");
				for (;;) {
					if (!DataCache.isEmpty()) {
						String str = DataCache.poll();
						GetResponse response = client
								.prepareGet(index, type, str).execute()
								.actionGet();

						if (response.isExists()) {
							logger.info("es中存在该id:{}", response.getId());
						} else {
							DataCache.nAdd(str);
						}
					} else {
						Iterator<String> it = DataCache.nIterator();
						while (it.hasNext()) {
							logger.info("es中不存在id:{}", it.next());
						}
						DataCache.nClear();
					}
				}
			}
		});
		th.start();
	}

	public void close() {
		client.close();
	}

}
