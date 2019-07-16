package com.wf.ew.light.utils;


import com.wf.ew.light.model.Lamp;
import com.wf.ew.light.model.Operate;
import com.wf.ew.light.service.LampService;
import com.wf.ew.light.service.OperateService;

/**
 * 路灯操作工具类，记录了谁开灯和关灯,无状态。
 * @author Administrator
 *
 */
public class OperateUtil {

	/**
	 * 普通开灯和关灯
	 * @param operateService
	 * @param lampService
	 * @param lamp
	 * @param opencloselamp
	 * @param nickname
	 */
	public static void operateRecordhelp(OperateService operateService,LampService lampService,Lamp lamp,boolean opencloselamp,String nickname) {
		//操作开始
		Operate operate = new Operate();
		operate.setState(0);
		operate.setNickname(nickname);
		Lamp byId = lampService.getById(lamp.getLampId());
		if (opencloselamp == true) {
			//开灯操作
			operate.setOperatecontent(byId.getNickName()+",开灯");
			
		}else {
			//关灯操作
			operate.setOperatecontent(byId.getNickName()+",关灯");
		}
		operateService.add(operate);
		
		//操作结束
	}
	
	/**
	 * 一键开灯和关灯
	 * @param operateService
	 * @param opencloselamp
	 * @param nickname
	 */
	public static void operateRecordOneKeyhelp(OperateService operateService,boolean opencloselamp,String nickname) {
		//操作开始
		Operate operate = new Operate();
		operate.setState(0);
		operate.setNickname(nickname);
		
		if (opencloselamp == true) {
			//开灯操作
			operate.setOperatecontent("一键开灯");
			
		}else {
			//关灯操作
			operate.setOperatecontent("一键关灯");
		}
		operateService.add(operate);
		
		//操作结束
	}
	
}
