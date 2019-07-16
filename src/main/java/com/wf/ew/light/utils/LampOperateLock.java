package com.wf.ew.light.utils;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 路灯操作全局锁工具类，单例模式
 * @author 
 *
 */
public class LampOperateLock {

	private static ReentrantLock lock = new ReentrantLock();
	
	public static ReentrantLock getLock() {
		return lock;
	}

	private int status = 0;
	
	private int mystatus = 0;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getMystatus() {
		return mystatus;
	}

	public void setMystatus(int mystatus) {
		this.mystatus = mystatus;
	}

	/**
	 * 设置单例对象
	 */
	private static  class SingletonClassInstance{
    	private static final LampOperateLock instance = new LampOperateLock();
    }
    
    private LampOperateLock() {}
    
    public static LampOperateLock getInstance() {
    	return SingletonClassInstance.instance;
    }
	
}
