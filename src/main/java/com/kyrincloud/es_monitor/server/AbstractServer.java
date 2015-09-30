package com.kyrincloud.es_monitor.server;

import com.kyrincloud.es_monitor.datasource.DatabaseMonitor;
import com.kyrincloud.es_monitor.elasticsearch.EsMonitor;

public abstract class AbstractServer {

	DatabaseMonitor databaseMonitor;
	EsMonitor esMonitor;

	public AbstractServer(DatabaseMonitor databaseMonitor, EsMonitor esMonitor) {
		this.databaseMonitor = databaseMonitor;
		this.esMonitor = esMonitor;
	}

}