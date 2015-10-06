package com.kyrincloud.es_monitor.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyrincloud.es_monitor.config.PropertiesConfig;
import com.kyrincloud.es_monitor.datasource.DatabaseMonitor;
import com.kyrincloud.es_monitor.elasticsearch.EsMonitor;
import com.kyrincloud.es_monitor.server.MonitorServer;

/**
 * 
 * @author kyrin
 * @date 2015年9月28日
 */
public class Bootstrap {
	
	private static Logger logger=LoggerFactory.getLogger(Bootstrap.class);

	public static void main(String[] args) {
		logger.info("服务启动……");
		PropertiesConfig config=new PropertiesConfig();
		DatabaseMonitor databaseMonitor = new DatabaseMonitor(config.getDataSource(),config.getHeart());
		EsMonitor esMonitor = new EsMonitor(config.getElasticsearch());
		MonitorServer monitor = new MonitorServer(databaseMonitor, esMonitor);
		monitor.start();
		logger.info("服务启动完毕");
	}
}
