package com.kyrincloud.es_monitor.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.kyrincloud.es_monitor.config.PropertyConfigFactory;

/**
 * 数据源实例
 * @author kyrin
 * @date 2015年9月28日
 *
 */
public class PgDataSource {
	private static final String url = PropertyConfigFactory.getInstance().getProperties().getProperty("pg.datasource");
	private static final String username = PropertyConfigFactory.getInstance().getProperties()
			.getProperty("pg.username");
	private static final String password = PropertyConfigFactory.getInstance().getProperties()
			.getProperty("pg.password");

	public PgDataSource() {

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
