package com.kyrincloud.es_monitor.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.kyrincloud.es_monitor.queue.DataCache;

public final class DatabaseMonitor {

	private String table_name;
	private String table_key;
	private String sql;

	public DatabaseMonitor(String table_name, String table_key) {
		this.table_name = table_name;
		this.table_key = table_key;

	}

	private List<String> executor(int pageIndex, int pageSize) {
		sql = "select " + table_key + " from " + table_name + "  limit " + pageSize + " offset " + (pageIndex - 1)
				* pageSize;
		List<String> list = new ArrayList<String>();
		Connection con = DataSourcePool.getInstance().getConnection();
		try {
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet res = stmt.executeQuery(sql);
			if (res != null) {
				while (res.next()) {
					String str = res.getString(table_key);
					list.add(str);
					System.out.println(str);
				}
			}
			stmt.close();
			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourcePool.getInstance().release(con);
		}
		return list;
	}

	private long count() {
		long num = 0;
		Connection con = DataSourcePool.getInstance().getConnection();
		try {
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet res = stmt.executeQuery("select count(*) from " + table_name);
			res.next();
			if (res != null && res.first()) {
				num = res.getLong(1);
			}
			stmt.close();
			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourcePool.getInstance().release(con);
		}
		return num;
	}

	public void start() {
		Timer task = new Timer(true);
		task.schedule(new TimerTask() {
			@Override
			public void run() {
				int pageIndex = 0;
				int pageSize = 100;
				long total = count();
				int allPage = (int) ((total + pageSize - 1) / pageSize);
				for (; allPage > 0; allPage--) {
					List<String> list = executor(++pageIndex, 100);
					for (String str : list) {
						DataCache.add(str);
					}
				}
			}
		}, 1000 * 10);
	}
}
