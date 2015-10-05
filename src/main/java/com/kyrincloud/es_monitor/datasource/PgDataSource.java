package com.kyrincloud.es_monitor.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.kyrincloud.es_monitor.config.PropertyConfigFactory;
import com.kyrincloud.es_monitor.util.Util;

/**
 * 数据源实例
 * @author kyrin
 * @date 2015年9月28日
 *
 */
public class PgDataSource {
	private static  String url;
	private static  String username ;
	private static  String password ;

	@SuppressWarnings("static-access")
	public PgDataSource() {
		 String _url = PropertyConfigFactory.getInstance().getProperties().getProperty("pg.datasource");
		 if(Util.validate("pg.datasource", _url)){
			 this.url=_url;
		 }
		 String _username = PropertyConfigFactory.getInstance().getProperties()
				.getProperty("pg.username");
		 if(Util.validate("pg.username", _username)){
			 this.username=_username;
		 }
		 String _password = PropertyConfigFactory.getInstance().getProperties()
				.getProperty("pg.password");
		 if(Util.validate("pg.password", _password)){
			 this.password=_password;
		 }
	}

	public synchronized Connection connection() {
		Connection con = null;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

	public synchronized void close(Connection con) {
		try {
			if (con != null && !con.isClosed()) {
				con.isClosed();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
