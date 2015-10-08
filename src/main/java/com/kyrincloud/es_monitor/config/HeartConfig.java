package com.kyrincloud.es_monitor.config;

/**
 * 心跳参数（数据库查询频率和es心跳频率）
 * @author kyrin
 *
 */
public class HeartConfig {

	//单位：分钟
	private int _heartRate;

	public HeartConfig(int heartRate) {
		this._heartRate = heartRate;
	}

	public HeartConfig() {

	}

	public int get_heartRate() {
		return _heartRate;
	}

	public void set_heartRate(int _heartRate) {
		this._heartRate = _heartRate;
	}
}
