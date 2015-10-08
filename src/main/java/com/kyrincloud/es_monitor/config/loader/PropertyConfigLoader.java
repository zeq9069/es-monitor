package com.kyrincloud.es_monitor.config.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 配置文件加载类
 * @author kyrin
 * @date 2015年9月28日
 *
 */
public class PropertyConfigLoader {

	private static Properties properties = new Properties();

	private static final String PATH = System.getProperty("config");

	public PropertyConfigLoader() {
		FileInputStream config = null;
		try {
			if (PATH == null || PATH.isEmpty()) {
				throw new FileNotFoundException("缺少配置文件,请指定配置文件参数：-Dconfig=<config.properties路径>");
			}
			if (!PATH.endsWith(".properties")) {
				throw new IllegalArgumentException("配置文件必须是properties类型");
			}

			//如果是绝对路径
			if (PATH.startsWith("/") || PATH.indexOf(":") > 0) {
				config = new FileInputStream(new File(PATH));
			} else {
				config = new FileInputStream(new File(System.getProperty("user.dir") + "/" + PATH));
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(0);
		}

		try {
			if (config != null) {
				properties.load(config);
			} else {
				throw new IOException("加载配置文件失败");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private static class Instance {
		private static final PropertyConfigLoader factory = new PropertyConfigLoader();
	}

	public static final PropertyConfigLoader getInstance() {
		return Instance.factory;
	}

	@SuppressWarnings("static-access")
	public Properties getProperties() {
		return this.properties;
	}

}
