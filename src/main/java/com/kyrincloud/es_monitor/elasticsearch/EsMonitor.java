package com.kyrincloud.es_monitor.elasticsearch;

import java.net.Socket;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

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
import com.kyrincloud.es_monitor.config.HeartConfig;
import com.kyrincloud.es_monitor.queue.DataCache;

/**
 * 
 * @author kyrin 
 * @date 2015年9月30日
 */
public final class EsMonitor implements Monitor {

	private static Logger logger = LoggerFactory.getLogger(EsMonitor.class);
	private ElasticsearchConfig config;
	private HeartConfig heart;
	private Client client;
	private boolean isRearchable = false;

	@SuppressWarnings("resource")
	public EsMonitor(ElasticsearchConfig config, HeartConfig heart) {
		this.config = config;
		this.heart = heart;
		//判断es服务是否可用
		if (isRearch()) {
			Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", config.get_clusterName())
					.build();
			client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(config
					.get_server(), config.get_port()));
		}
	}

	public void start() {
		Heartbeat();
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				logger.info("elasticsearch服务检测开始");
				for (;;) {
					if (!DataCache.isEmpty() && isRearchable) {
						String str = DataCache.poll();
						GetResponse response = client.prepareGet(config.get_index(), config.get_type(), str).execute()
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

	//es服务金检测是否可达
	private boolean isRearch() {
		try {
			Socket s = new Socket(config.get_server(), config.get_port());
			isRearchable = true;
			s.close();
		} catch (Exception e) {
			logger.error("连接es服务失败：{}", e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
		return true;
	}

	//心跳检测
	private void Heartbeat() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				isRearch();
				logger.info("es服务心跳检测开始");
			}
		}, 0, 1000 * 60 * heart.get_heartRate());

	}

	public void close() {
		client.close();
	}

}
