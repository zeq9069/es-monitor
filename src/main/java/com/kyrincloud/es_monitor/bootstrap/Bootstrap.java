package com.kyrincloud.es_monitor.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyrincloud.es_monitor.config.PropertyConfigFactory;
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

	private static String table_name=PropertyConfigFactory.getInstance().getProperties().getProperty("pg.table");
	private static String table_key=PropertyConfigFactory.getInstance().getProperties().getProperty("pg.table.key");
	private static String rate=PropertyConfigFactory.getInstance().getProperties().getProperty("heart.rate");
	
	public static void main(String[] args) {
		logger.info("服务启动……");
		DatabaseMonitor databaseMonitor = new DatabaseMonitor(table_name, table_key,1000,Integer.parseInt(rate));
		EsMonitor esMonitor = new EsMonitor();
		MonitorServer monitor = new MonitorServer(databaseMonitor, esMonitor);
		monitor.start();
		logger.info("服务启动完毕");
	}
}
