package com.kyrincloud.es_monitor.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public final class DataSourcePool {

	private int max;

	private int min;

	private Queue<Connection> pools = new ArrayBlockingQueue<Connection>(5);

	private PgDataSource pds;

	private AtomicInteger isUsed = new AtomicInteger(0);

	public DataSourcePool(PgDataSource pds, int max, int min) {
		this.pds = pds;
		this.max = max;
		this.min = min;
	}

	public DataSourcePool(PgDataSource pds) {
		this(pds, 5, 0);
	}

	private static final class Instance {
		private static final DataSourcePool p = new DataSourcePool(new PgDataSource(), 5, 1);
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
}
