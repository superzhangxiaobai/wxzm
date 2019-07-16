package com.wf.ew.light.utils;

/**
 * 页面定时刷新，记录是否有路灯状态改变工具类，单例模式,全局唯一
 * @author 
 *
 */
public class OperateState {
	
	private String state = "none";//have

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 设置单例对象
	 */
	private static  class SingletonClassInstance{
    	private static final OperateState instance = new OperateState();
    }
    
    private OperateState() {}
    
    public static OperateState getInstance() {
    	return SingletonClassInstance.instance;
    }
	
}
