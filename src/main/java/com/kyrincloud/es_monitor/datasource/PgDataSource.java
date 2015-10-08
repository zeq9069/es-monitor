package com.kyrincloud.es_monitor.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyrincloud.es_monitor.config.DataSourceConfig;

/**
 * 数据源实例
 * @author kyrin
 * @date 2015年9月28日
 *
 */
public class PgDataSource {

	Logger logger = LoggerFactory.getLogger(PgDataSource.class);

	private DataSourceConfig config;

	public PgDataSource(DataSourceConfig config) {
		this.config = config;
	}

	public synchronized Connection connection() {
		Connection con = null;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(config.get_dataSource(), config.get_username(), config.get_password());
		} catch (ClassNotFoundException e) {
			logger.error("数据库加载驱动失败:{}", e.getMessage());
			e.printStackTrace();
			System.exit(0);
		} catch (SQLException e) {
			logger.error("获取数据库连接失败:{}", e.getMessage());
			e.printStackTrace();
			System.exit(0);
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
