package com.kyrincloud.es_monitor.config;

/**
 * 数据源配置项
 * @author kyrin
 * @date 2015年10月7日
 *
 */
public class DataSourceConfig {

	private String _dataSource; //数据源url
	private String _username;//用户名
	private String _password;//密码
	private String _table;//表名称
	private String _tableKey;//表的primary key
	private int _pools;//连接池最大数

	public DataSourceConfig() {
	}

	public DataSourceConfig(String dataSource, String username, String password, String tableName, String tableKey,
			int pools) {
		this._dataSource = dataSource;
		this._username = username;
		this._password = password;
		this._table = tableName;
		this._tableKey = tableKey;
		this._pools = pools;
	}

	public String get_dataSource() {
		return _dataSource;
	}

	public String get_username() {
		return _username;
	}

	public String get_password() {
		return _password;
	}

	public String get_table() {
		return _table;
	}

	public String get_tableKey() {
		return _tableKey;
	}

	public int get_pools() {
		return _pools;
	}

	public void set_dataSource(String _dataSource) {
		this._dataSource = _dataSource;
	}

	public void set_username(String _username) {
		this._username = _username;
	}

	public void set_password(String _password) {
		this._password = _password;
	}

	public void set_table(String _table) {
		this._table = _table;
	}

	public void set_tableKey(String _tableKey) {
		this._tableKey = _tableKey;
	}

	public void set_pools(int _pools) {
		this._pools = _pools;
	}

}
