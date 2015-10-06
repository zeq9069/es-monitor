package com.kyrincloud.es_monitor.config;

public class HeartConfig {
	
	private  int _heartRate;
	
	public HeartConfig(int heartRate) {
		this._heartRate=heartRate;
	}
	
	public HeartConfig (){
		
	}

	public int get_heartRate() {
		return _heartRate;
	}

	public void set_heartRate(int _heartRate) {
		this._heartRate = _heartRate;
	}
}
