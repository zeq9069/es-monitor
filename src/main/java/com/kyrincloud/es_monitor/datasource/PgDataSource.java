package com.kyrincloud.es_monitor.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.kyrincloud.es_monitor.config.DataSourceConfig;

/**
 * 数据源实例
 * @author kyrin
 * @date 2015年9月28日
 *
 */
public class PgDataSource {
	private  String url;
	private  String username ;
	private  String password ;

	public PgDataSource(DataSourceConfig config) {
			 this.url=config.get_dataSource();
			 this.username=config.get_username();
			 this.password=config.get_password();
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
