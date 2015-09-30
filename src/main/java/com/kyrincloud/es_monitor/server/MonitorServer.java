package com.kyrincloud.es_monitor.server;

import com.kyrincloud.es_monitor.datasource.DatabaseMonitor;
import com.kyrincloud.es_monitor.elasticsearch.EsMonitor;

/**
 * 监控服务
 * @author kyrin
 *
 */
public class MonitorServer {

	DatabaseMonitor databaseMonitor;
	EsMonitor esMonitor;

	public MonitorServer(DatabaseMonitor databaseMonitor, EsMonitor esMonitor) {
		this.databaseMonitor = databaseMonitor;
		this.esMonitor = esMonitor;
	}

	public void start() {
		databaseMonitor.start();
		esMonitor.start();
	}
}
