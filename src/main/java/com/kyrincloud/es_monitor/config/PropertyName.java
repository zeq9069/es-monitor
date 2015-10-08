package com.kyrincloud.es_monitor.config;

/**
 * 所有配置参数
 * @author kyrin
 *
 */
public class PropertyName {
	/**
	 * elasticsearch
	 */
	public static final String ES_SERVER = "elasticsearch.server";
	public static final String ES_PORT = "elasticsearch.port";
	public static final String ES_CLUSTER_NAME = "elasticsearch.cluster.name";
	public static final String ES_INDEX = "elasticsearch.index";
	public static final String ES_TYPE = "elasticsearch.type";

	/**
	 * datasource
	 */
	public static final String DB_DATASOURCE = "pg.datasource";
	public static final String DB_USERNAME = "pg.username";
	public static final String DB_PASSWORD = "pg.password";
	public static final String DB_TABLE_NAME = "pg.table";
	public static final String DB_TABLE_KEY = "pg.table.key";
	public static final String DB_DATASOURCE_POOLS = "pg.datasource.pools";

	/**
	 * heart
	 */
	public static final String HEART_RATE = "heart.rate";

}
