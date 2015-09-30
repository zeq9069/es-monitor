package com.kyrincloud.es_monitor.queue;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 数据缓存队列
 * @author kyrin
 * @date 2015年9月30日
 *
 */
public class DataCache {

	private static Queue<String> data = new ArrayBlockingQueue<String>(10000000);

	public static String poll() {
		return data.poll();
	}

	public static boolean add(String e) {
		return data.add(e);
	}

	public static boolean isEmpty() {
		return data.isEmpty();
	}

	public static int size() {
		return data.size();
	}

	public static boolean remove(Object obj) {
		return data.remove(obj);
	}

}
