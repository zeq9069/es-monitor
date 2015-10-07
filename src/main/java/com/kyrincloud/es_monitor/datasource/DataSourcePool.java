package com.kyrincloud.es_monitor.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.kyrincloud.es_monitor.config.DataSourceConfig;
import com.kyrincloud.es_monitor.config.PropertiesConfig;

public final class DataSourcePool {

	private Queue<Connection> pools;

	private PgDataSource pds;

	private AtomicInteger isUsed = new AtomicInteger(0);
	
	private static DataSourceConfig dataSource=new PropertiesConfig().getDataSource();

	public DataSourcePool(PgDataSource pds) {
		this.pds = pds;
		pools= new ArrayBlockingQueue<Connection>(dataSource.get_pools());
	}

	private static final class Instance {
		private static final DataSourcePool p = new DataSourcePool(new PgDataSource(new PropertiesConfig().getDataSource()));
	}

	public static final DataSourcePool getInstance() {
		return DataSourcePool.Instance.p;
	}

	protected Connection getConnection() {
		if (!pools.isEmpty()) {
			Connection con = pools.poll();
			try {
				if (con != null && !con.isClosed()) {
					isUsed.incrementAndGet();
					return con;
				} else {
					pools.remove(con);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return getConnection();
		} else{
			isUsed.incrementAndGet();
			return pds.connection();
		}
	}

	protected void release(Connection con) {
		if (pools.size()+isUsed.get() >= dataSource.get_pools()) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			isUsed.decrementAndGet();
			pools.add(con);
		}
	}
	
	protected int size() {
		return pools.size();
	}
	
}
