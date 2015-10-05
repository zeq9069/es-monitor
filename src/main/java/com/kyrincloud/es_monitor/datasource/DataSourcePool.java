package com.kyrincloud.es_monitor.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.kyrincloud.es_monitor.config.PropertyConfigFactory;
import com.kyrincloud.es_monitor.util.Util;

public final class DataSourcePool {

	private  int max;

	private Queue<Connection> pools;

	private PgDataSource pds;

	private AtomicInteger isUsed = new AtomicInteger(0);

	public DataSourcePool(PgDataSource pds) {
		this.pds = pds;
		String _max=PropertyConfigFactory.getInstance().getProperties().getProperty("pg.datasource.pools");
		if(Util.validate("pg.datasource.pools", _max)){
			this.max=Integer.parseInt(_max);
		}
		 pools= new ArrayBlockingQueue<Connection>(max);
	}

	private static final class Instance {
		private static final DataSourcePool p = new DataSourcePool(new PgDataSource());
	}

	public static final DataSourcePool getInstance() {
		return DataSourcePool.Instance.p;
	}

	protected Connection getConnection() {
		if (!pools.isEmpty() && isUsed.get() + pools.size() >= max) {
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
		} else {
			isUsed.incrementAndGet();
			return pds.connection();
		}
	}

	protected void release(Connection con) {
		if (pools.size() >= max) {
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
