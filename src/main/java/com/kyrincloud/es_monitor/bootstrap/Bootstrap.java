package com.kyrincloud.es_monitor.bootstrap;

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

	private static String table_name = PropertyConfigFactory.getInstance().getProperties().getProperty("pg.table");
	private static String table_key = PropertyConfigFactory.getInstance().getProperties().getProperty("pg.table.key");

	public static void main(String[] args) {
		DatabaseMonitor databaseMonitor = new DatabaseMonitor(table_name, table_key);
		EsMonitor esMonitor = new EsMonitor();
		MonitorServer monitor = new MonitorServer(databaseMonitor, esMonitor);
		System.out.println("服务启动……");
		monitor.start();
		System.out.println("服务启动完毕");

	}
}
