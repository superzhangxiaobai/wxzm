package com.wf.ew.light.utils;

import java.util.ArrayList;
import java.util.List;

import com.wf.ew.light.model.Lamp;
import com.wf.ew.light.service.LampService;

/**
 * IP直连同步子线程
 * @author Administrator
 *
 */
public class DirectThreadUtil implements Runnable {
	
	private LampService lampService;
	
	private Lamp lamp;
	
	private List<Lamp> list;
	
	private String addr;

	public DirectThreadUtil(LampService lampService, Lamp lamp, List<Lamp> list,String addr) {
		super();
		this.lampService = lampService;
		this.lamp = lamp;
		this.list = list;
		this.addr = addr;
	}

	@Override
	public void run() {
		System.out.println("IP直连同步子线程====>"+Thread.currentThread().getName());
		try {
			/** IP多线程同步加锁 */
			DirectOperateLock.getInstance().getLock().lock();
			/** 同一组灯IP地址和通道相同，首先查询状态 */
			ResponseResult directStateHelp = IPDirectUtil.directStateHelp(addr, lamp);
			/** 表示已开灯 */
			if (directStateHelp.getCode() == 200 && directStateHelp.getObj() == 1) {
				List<Lamp> sendData = new ArrayList<>() ;
				for (Lamp linshi : list) {
					if (linshi.getLampBefore() == 1) {
						System.out.println("已经开灯，不需要保存数据库-------------------");
						continue;
					}
					linshi.setLampAfter(1);
					linshi.setLampLeft(1);
					linshi.setLampRight(1);
					linshi.setLampBefore(1);
					linshi.setCode("1111");
					sendData.add(linshi);
				}
				if (sendData.size()>0) {
					boolean result = lampService.updateBatch(sendData);
				}
				
				OperateState.getInstance().setState("have");
			}else if (directStateHelp.getCode() == 200 && directStateHelp.getObj() == 0) {
				List<Lamp> sendData = new ArrayList<>() ;
				for (Lamp linshi : list) {
					if (linshi.getLampBefore() == 0) {
						System.out.println("已经关灯，不需要保存数据库-------------------");
						continue;
					}
					linshi.setLampAfter(0);
					linshi.setLampLeft(0);
					linshi.setLampRight(0);
					linshi.setLampBefore(0);
					linshi.setCode("0000");
					sendData.add(linshi);
				}
				if (sendData.size()>0) {
					boolean result = lampService.updateBatch(sendData);
				}
				/** 添加操作状态 */
				OperateState.getInstance().setState("have");
			}
		} finally {
			/** IP多线程同步释放锁 */
			DirectOperateLock.getInstance().getLock().unlock();
		}
		
	}

}
