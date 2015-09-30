package com.kyrincloud.es_monitor.queue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 数据缓存队列
 * @author kyrin
 * @date 2015年9月30日
 *
 */
public class DataCache {

	private static Queue<String> data = new ArrayBlockingQueue<String>(1000);
	private static List<String> notExist=new ArrayList<String>();//存放数据库中不存在es服务中的id

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
	
	public static boolean nAdd(String e) {
		return notExist.add(e);
	}
	
	public static String get(int index){
		return notExist.get(index);
	}

	public static boolean nIsEmpty() {
		return notExist.isEmpty();
	}

	public static int nSize() {
		return notExist.size();
	}

	public static boolean nRemove(Object obj) {
		return notExist.remove(obj);
	}
	
	public static Iterator<String> nIterator(){
		return notExist.iterator();
	}
	
	public static void nClear() {
		notExist.clear();
	}

}
