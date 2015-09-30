package com.kyrincloud.es_monitor.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyrincloud.es_monitor.queue.DataCache;

public final class DatabaseMonitor {
	
	private static Logger logger=LoggerFactory.getLogger(DatabaseMonitor.class);
	private String table_name;
	private String table_key;
	private String sql;
	private long delay;
	private long period;
	private int pageSize=100;

	public DatabaseMonitor(String table_name, String table_key,long delay,long period) {
		this.table_name = table_name;
		this.table_key = table_key;
		this.delay=delay;
		this.period=period;
	}
	
	public DatabaseMonitor(String table_name, String table_key) {
		this(table_name,table_key,1000,1000*30);
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
			logger.error("查询数据库时，出现异常：{}",e.getMessage());
			e.printStackTrace();
		} finally {
			logger.info("结束查询，关闭数据库连接");
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
				logger.info("[数据库查询任务启动]");
				int pageIndex = 0;
				long total = count();
				int allPage = (int) ((total + pageSize - 1) / pageSize);
				for (; allPage > 0; allPage--) {
					List<String> list = executor(++pageIndex, 100);
					for (String str : list) {
						DataCache.add(str);
					}
				}
				logger.info("[数据库查询任务结束，共查询：{}条,连接池数量：{}]",total,DataSourcePool.getInstance().size());
			}
		}, delay,period );
	}
}