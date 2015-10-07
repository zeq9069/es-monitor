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

import com.kyrincloud.es_monitor.Monitor;
import com.kyrincloud.es_monitor.config.DataSourceConfig;
import com.kyrincloud.es_monitor.config.HeartConfig;
import com.kyrincloud.es_monitor.queue.DataCache;

public final class DatabaseMonitor implements Monitor{
	
	private static Logger logger=LoggerFactory.getLogger(DatabaseMonitor.class);
	private String sql;
	private long delay;
	private int pageSize;
	private DataSourceConfig config;
	private HeartConfig heart;

	public DatabaseMonitor(DataSourceConfig config,HeartConfig heart) {
		this(config,heart,1000);
	}
	
	public DatabaseMonitor(DataSourceConfig config,HeartConfig heart,int delay) {
		this(config,heart,1000,1000);
	}
	
	public DatabaseMonitor(DataSourceConfig config,HeartConfig heart,int delay,int pageSize) {
		this.config=config;
		this.heart=heart;
		this.delay=delay;
		this.pageSize=pageSize;
	}
	
	
	private List<String> executor(int pageIndex, int pageSize) {
		sql = "select " + config.get_tableKey() + " from " + config.get_table() + "  limit " + pageSize + " offset " + (pageIndex - 1)
				* pageSize;
		List<String> list = new ArrayList<String>();
		Connection con = DataSourcePool.getInstance().getConnection();
		try {
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet res = stmt.executeQuery(sql);
			if (res != null) {
				while (res.next()) {
					String str = res.getString(config.get_tableKey());
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
			logger.info("结束查询，释放数据库连接");
			DataSourcePool.getInstance().release(con);
		}
		return list;
	}

	private long count() {
		long num = 0;
		Connection con = DataSourcePool.getInstance().getConnection();
		try {
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet res = stmt.executeQuery("select count(*) from " + config.get_table());
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
					List<String> list = executor(++pageIndex, pageSize);
					for (String str : list) {
						DataCache.put(str);
					}
				}
				logger.info("[数据库查询任务结束，共查询：{}条,连接池数量：{}]",total,DataSourcePool.getInstance().size());
			}
		}, delay,heart.get_heartRate()*1000*60 );
	}
}
