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


import com.kyrincloud.es_monitor.Monitor;
import com.kyrincloud.es_monitor.config.ElasticsearchConfig;
import com.kyrincloud.es_monitor.queue.DataCache;

/**
 * 
 * @author kyrin 
 * @date 2015年9月30日
 */
public final class EsMonitor implements Monitor{

	private static Logger logger = LoggerFactory.getLogger(EsMonitor.class);
	private ElasticsearchConfig config;
	private Client client;

	@SuppressWarnings("resource")
	public EsMonitor(ElasticsearchConfig config) {
			this.config=config;
		Settings settings = ImmutableSettings
				.settingsBuilder()
				.put("cluster.name",config.get_clusterName())
				.build();
		client = new TransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(config.get_server(),
						config.get_port()));
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
								.prepareGet(config.get_index(), config.get_type(), str).execute()
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
