package com.kyrincloud.es_monitor.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 配置参数
 * @author kyrin
 * @date 2015年9月28日
 *
 */
public class PropertyConfigFactory {

	private static Properties properties = new Properties();

	private static final String PATH = System.getProperty("config");

	public PropertyConfigFactory() {
		FileInputStream config=null;
		try {
			if(PATH==null || PATH.isEmpty()){
				throw new FileNotFoundException("缺少配置文件,请指定配置文件参数：-Dconfig=config.properties");
			}
			//如果是绝对路径
			if (PATH.startsWith("/") || PATH.indexOf(":") > 0) {
				config = new FileInputStream(new File(PATH));
			}else{
				config = new FileInputStream(new File(System.getProperty("user.dir")+"/"+PATH));
			}
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
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
