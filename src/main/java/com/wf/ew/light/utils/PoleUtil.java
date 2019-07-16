package com.wf.ew.light.utils;

import java.util.ArrayList;
import java.util.List;

import com.wf.ew.common.PageResult;
import com.wf.ew.light.model.Lamp;
import com.wf.ew.light.service.LampService;

public class PoleUtil {

	/**
	 * 灯杆状态异常设置
	 * @param lamp
	 * @param lampService
	 * @return
	 */
	public static boolean updatePoleState(Lamp lamp,LampService lampService) {
		boolean result = false ;
		if (lamp.getCategory().startsWith("路灯") || lamp.getCategory().startsWith("顶灯")) {
			PageResult<Lamp> glist = lampService.listLamp(1, 500, "group_ing", lamp.getGrouping());
			List<Lamp> data = glist.getData();
			List<Lamp> sendData = new ArrayList<>() ;
			for (Lamp linshi : data) {
				linshi.setRemark2(-1);
				sendData.add(linshi);
			}
			result = lampService.updateBatch(sendData);
			
			
			
		} else {
			if (lamp.getSecondlevel() != null && lamp.getSecondlevel().startsWith("同城")) {
				System.out.println("------------------同城逻辑[灯杆状态]---------------------------");
				
				PageResult<Lamp> glist = lampService.listLamp(1, 500, "memo1", lamp.getMemo1());
				List<Lamp> data = glist.getData();
				List<Lamp> sendData = new ArrayList<>() ;
				for (Lamp linshi : data) {
					linshi.setRemark2(-1);
					sendData.add(linshi);
				}
				result = lampService.updateBatch(sendData);
				
				
			} else {
				System.out.println("-----------------------非同城灯塔逻辑[灯杆状态]-------------------------------");
				Lamp byId = lampService.getById(lamp.getLampId());
				byId.setRemark2(-1);
				result=lampService.update(byId);
				
			}
			
		}
		
		return result;
	}
	
	/**
	 * 一键开关灯，灯杆状态异常设置
	 * @param lamp
	 * @param lampService
	 * @return
	 */
	public static boolean updatePoleStateOneKey(Lamp lamp,LampService lampService) {
		boolean result = false ;
		Lamp byId = lampService.getById(lamp.getLampId());
		byId.setRemark2(-1);
		result=lampService.update(byId);
		return result;
	}
	
}
