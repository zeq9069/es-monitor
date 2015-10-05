package com.kyrincloud.es_monitor.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置参数
 * @author kyrin
 * @date 2015年9月28日
 *
 */
public class PropertyConfigFactory {

	private static Properties properties = new Properties();

	private static final String CONFIG = "config.properties";

	public PropertyConfigFactory() {
		ClassLoader load = PropertyConfigFactory.class.getClassLoader();
		InputStream config = load.getResourceAsStream(CONFIG);
		try {
			if (config != null){
				properties.load(config);
			}else{
				throw new IOException("找不到配置文件:config.properties");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static class Instance {
		private static final PropertyConfigFactory factory = new PropertyConfigFactory();
	}

	public static final PropertyConfigFactory getInstance() {
		return Instance.factory;
	}

	@SuppressWarnings("static-access")
	public Properties getProperties() {
		return this.properties;
	}

}
