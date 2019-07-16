package com.wf.ew.light.utils;

import java.util.ArrayList;
import java.util.List;

import com.wf.ew.light.model.Lamp;

/**
 * 一键开灯，一键关灯，启动过程，路灯缓存工具类,单例模式
 * @author Administrator
 *
 */
public class OneKey {

	private List<Lamp> list = new ArrayList<>();
	
	public List<Lamp> getList() {
		return list;
	}

	public void addList(Lamp lamp) {
		this.list.add(lamp);
	}
	public void clear() {
		if (list != null) {
			list.clear();
		}
	}

	/**
	 * 设置单例对象
	 */
	private static  class SingletonClassInstance{
    	private static final OneKey instance = new OneKey();
    }
    
    private OneKey() {}
    
    public static OneKey getInstance() {
    	return SingletonClassInstance.instance;
    }
	
}
