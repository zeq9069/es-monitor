package com.kyrincloud.es_monitor.config;

import java.util.Properties;

import com.kyrincloud.es_monitor.config.loader.PropertyConfigLoader;
import com.kyrincloud.es_monitor.util.Util;

/**
 * 配置文件对象生成
 * @author kyrin
 *
 */
public class PropertiesConfig {

	private final DataSourceConfig dataSource;
	private final ElasticsearchConfig elasticsearch;
	private final HeartConfig heart;

	public PropertiesConfig() {
		Properties pro = PropertyConfigLoader.getInstance().getProperties();
		this.dataSource = loadDataSource(pro);
		this.elasticsearch = loadElasticsearch(pro);
		this.heart = loadHeart(pro);
	}

	public DataSourceConfig getDataSource() {
		return dataSource;
	}

	public ElasticsearchConfig getElasticsearch() {
		return elasticsearch;
	}

	public HeartConfig getHeart() {
		return heart;
	}

	private DataSourceConfig loadDataSource(Properties properties) {
		DataSourceConfig dsc = new DataSourceConfig();
		String dataSource = properties.getProperty(PropertyName.DB_DATASOURCE);
		if (Util.validate(PropertyName.DB_DATASOURCE, dataSource)) {
			dsc.set_dataSource(dataSource);
		}
		String username = properties.getProperty(PropertyName.DB_USERNAME);
		if (Util.validate(PropertyName.DB_USERNAME, username)) {
			dsc.set_username(username);
		}
		String password = properties.getProperty(PropertyName.DB_PASSWORD);
		if (Util.validate(PropertyName.DB_PASSWORD, password)) {
			dsc.set_password(password);
		}
		String tableName = properties.getProperty(PropertyName.DB_TABLE_NAME);
		if (Util.validate(PropertyName.DB_TABLE_NAME, tableName)) {
			dsc.set_table(tableName);
		}
		String tableKey = properties.getProperty(PropertyName.DB_TABLE_KEY);
		if (Util.validate(PropertyName.DB_TABLE_KEY, tableKey)) {
			dsc.set_tableKey(tableKey);
		}
		String pools = properties.getProperty(PropertyName.DB_DATASOURCE_POOLS);
		if (Util.validate(PropertyName.DB_DATASOURCE_POOLS, pools)) {
			dsc.set_pools(Integer.parseInt(pools));
		}
		return dsc;
	}

	private ElasticsearchConfig loadElasticsearch(Properties properties) {
		ElasticsearchConfig esc = new ElasticsearchConfig();

		String server = properties.getProperty(PropertyName.ES_SERVER);
		if (Util.validate(PropertyName.ES_SERVER, server)) {
			esc.set_server(server);
		}
		String port = properties.getProperty(PropertyName.ES_PORT);
		if (Util.validate(PropertyName.ES_PORT, port)) {
			esc.set_port(Integer.parseInt(port));
		}
		String clusterName = properties.getProperty(PropertyName.ES_CLUSTER_NAME);
		if (Util.validate(PropertyName.ES_CLUSTER_NAME, clusterName)) {
			esc.set_clusterName(clusterName);
		}
		String index = properties.getProperty(PropertyName.ES_INDEX);
		if (Util.validate(PropertyName.ES_INDEX, index)) {
			esc.set_index(index);
		}
		String type = properties.getProperty(PropertyName.ES_TYPE);
		if (Util.validate(PropertyName.ES_TYPE, type)) {
			esc.set_type(type);
		}
		return esc;
	}

	private HeartConfig loadHeart(Properties properties) {
		HeartConfig heart = new HeartConfig();
		String heartRate = properties.getProperty(PropertyName.HEART_RATE);
		if (Util.validate(PropertyName.HEART_RATE, heartRate)) {
			heart.set_heartRate(Integer.parseInt(heartRate));
		}
		return heart;
	}

}
