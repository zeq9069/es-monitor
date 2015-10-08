package com.kyrincloud.es_monitor.util;

/**
 * 辅助类
 * @author lenovo
 *
 */
public class Util {

	public static boolean validate(String name, String value) {
		if (value == null || value.isEmpty()) {
			throw new IllegalArgumentException(String.format("缺少参数:%s", name));
		}
		return true;
	}
}
