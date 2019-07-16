package com.wf.ew.light.utils;

import java.util.concurrent.locks.ReentrantLock;

/**
 * IP直连多线程锁
 * @author Administrator
 *
 */
public class DirectOperateLock {

	private ReentrantLock lock = new ReentrantLock();
	
	public ReentrantLock getLock() {
		return lock;
	}
	
	/**
	 * 设置单例对象
	 */
	private static class SingletonClassInstance{
		private static final DirectOperateLock instance = new DirectOperateLock();
	}
	
	private DirectOperateLock() {}
	
	public static DirectOperateLock getInstance() {
		return SingletonClassInstance.instance;
	}
	
}
