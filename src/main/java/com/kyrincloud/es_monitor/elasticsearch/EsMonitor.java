package com.kyrincloud.es_monitor.elasticsearch;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.kyrincloud.es_monitor.config.PropertyConfigFactory;
import com.kyrincloud.es_monitor.queue.DataCache;

/**
 * 
 * @author kyrin
 *`@date 2015年9月30日
 */
public final class EsMonitor {

	private static String host = PropertyConfigFactory.getInstance().getProperties()
			.getProperty("elasticsearch.server");
	private static String port = PropertyConfigFactory.getInstance().getProperties().getProperty("elasticsearch.port");
	private static String index = PropertyConfigFactory.getInstance().getProperties()
			.getProperty("elasticsearch.index");
	private static String type = PropertyConfigFactory.getInstance().getProperties().getProperty("elasticsearch.type");

	private Client client;

	@SuppressWarnings("resource")
	public EsMonitor() {
		Settings settings = ImmutableSettings
				.settingsBuilder()
				.put("cluster.name",
						PropertyConfigFactory.getInstance().getProperties().getProperty("elasticsearch.cluster.name"))
				.build();
		client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(host, Integer
				.parseInt(port)));
	}

	public void start() {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {

				for (;;) {
					if (!DataCache.isEmpty()) {
						String str = DataCache.poll();
						GetResponse response = client.prepareGet(index, type, str).execute().actionGet();
						if (response.getId() != null) {
							System.out.println("es中存在该id=" + response.getId());
						} else {
							System.out.println("es中不存在该id=" + response.getId());
						}
						System.out.println("datacache非空=" + str);
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
