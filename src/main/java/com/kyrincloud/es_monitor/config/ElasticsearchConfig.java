package com.kyrincloud.es_monitor.config;

/**
 * es服务配置项
 * @author kyrin
 * @date 2015年10月7日
 *
 */
public class ElasticsearchConfig {

	private String _server;//es服务ip地址
	private int _port;//es服务端口
	private String _clusterName;//es集群名称
	private String _index;//es服务索引
	private String _type;//es索引类型

	public ElasticsearchConfig() {
	}

	public ElasticsearchConfig(String server, int port, String clusterName, String index, String type) {
		this._server = server;
		this._port = port;
		this._clusterName = clusterName;
		this._index = index;
		this._type = type;
	}

	public String get_server() {
		return _server;
	}

	public int get_port() {
		return _port;
	}

	public String get_clusterName() {
		return _clusterName;
	}

	public String get_index() {
		return _index;
	}

	public String get_type() {
		return _type;
	}

	public void set_server(String _server) {
		this._server = _server;
	}

	public void set_port(int _port) {
		this._port = _port;
	}

	public void set_clusterName(String _clusterName) {
		this._clusterName = _clusterName;
	}

	public void set_index(String _index) {
		this._index = _index;
	}

	public void set_type(String _type) {
		this._type = _type;
	}

}
