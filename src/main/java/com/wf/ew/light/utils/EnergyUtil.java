package com.wf.ew.light.utils;

import java.util.List;

import com.wf.ew.common.PageResult;
import com.wf.ew.light.model.Energy;
import com.wf.ew.light.model.Lamp;
import com.wf.ew.light.service.EnergyService;
import com.wf.ew.light.service.LampService;

/**
 * 能耗统计工具类
 * @author 
 *
 */
public class EnergyUtil {

	/**
	 * 一键开灯和一键关灯能耗统计
	 * @param energyService
	 * @param lampService
	 * @param list
	 * @param opencloselamp
	 */
	public static void energyRecordOneKeyhelp(EnergyService energyService,LampService lampService,List<Lamp> list,boolean opencloselamp) {
		//能耗开始
		for (Lamp lamp : list) {
			if (lamp.getLampWarn() == 1) {
				// 开灯失败，跳过
				continue;
			}
			if (lamp.getSecondlevel()!=null&&lamp.getSecondlevel().startsWith("路灯")) {
				//灯塔带路灯
				energyRecordhelp(energyService, lampService, lamp, opencloselamp, 0);//灯塔统计
				energyRecordhelp(energyService, lampService, lamp, opencloselamp, 1);//灯塔路灯统计
			} else {
				/** 路灯，顶灯，普通灯塔统计 */
				energyRecordhelp(energyService, lampService, lamp, opencloselamp);
			}
			//energyRecordhelp(energyService,lampService,lamp,opencloselamp);
		}
		//能耗结束
	}
	
	/**
	 * 路灯，顶灯，普通灯塔能耗统计
	 * @param energyService
	 * @param lampService
	 * @param lamp
	 * @param opencloselamp
	 */
	public static void energyRecordhelp(EnergyService energyService,LampService lampService,Lamp lamp,boolean opencloselamp) {
		//能耗开始
		/** 根据路灯ID从能耗表中读取本路灯能耗记录列表，分页无效，查询所有 */
		PageResult<Energy> listEnergy = energyService.listEnergy(1, 500, "lamp_id", lamp.getLampId()+"");
		List<Energy> data = listEnergy.getData();
		/** 如果列表的最后一条数据状态有为零的，就修改 */
		if (data.size()>0&&data.get(data.size()-1).getState()==0) {
			data.get(data.size()-1).setState(1);
			/** 结束时间，以便统计开灯时长 */
			data.get(data.size()-1).setUpdateTime(null);
			energyService.update(data.get(data.size()-1));
		} else {
			Energy energy = new Energy() ;
			energy.setState(0);
			energy.setLampId(lamp.getLampId());
			Lamp byId2 = lampService.getById(lamp.getLampId());
			energy.setLampname(byId2.getNickName());
			energy.setRemark(byId2.getCategory().substring(0, 2));
			energy.setPower(byId2.getPower());
			/** 开灯操作才增加一条记录，如果关灯操作，即使最后一条记录没有状态为零的，也不增加能耗记录 */
			if (opencloselamp == true) {
				energyService.add(energy);
			}
			//energyService.add(energy);
		}
		//能耗结束
	}
	
	/**
	 * 灯塔能耗统计
	 * @param energyService
	 * @param lampService
	 * @param lamp
	 * @param opencloselamp
	 * @param codeType
	 */
	public static void energyRecordhelp(EnergyService energyService,LampService lampService,Lamp lamp,boolean opencloselamp,Integer codeType) {
		if (codeType == 0) {
			// 灯塔统计
			energyRecordhelp(energyService, lampService, lamp, opencloselamp);
		} else {
			//----------------------------------------------------start
			// 灯塔路灯统计
			/** 在能耗表中，灯塔的路灯ID为灯塔Id + 一万 */
			PageResult<Energy> listEnergy = energyService.listEnergy(1, 500, "lamp_id", (lamp.getLampId()+10000)+"");
			List<Energy> data = listEnergy.getData();
			if (data.size()>0&&data.get(data.size()-1).getState()==0) {
				data.get(data.size()-1).setState(1);
				data.get(data.size()-1).setUpdateTime(null);
				energyService.update(data.get(data.size()-1));
			} else {
				Energy energy = new Energy() ;
				energy.setState(0);
				energy.setLampId(lamp.getLampId()+10000);//灯塔的路灯id后面加一万，和灯塔区别
				Lamp byId2 = lampService.getById(lamp.getLampId());
				energy.setLampname(byId2.getNickName()+"[路灯]");
				energy.setPower(byId2.getSecondpower());
				energy.setRemark("路灯");
				if (opencloselamp == true) {
					energyService.add(energy);
				}
				//energyService.add(energy);
			}
			//----------------------------------------------------end
		}
	}
	
	/**
	 * 路灯和顶灯能耗统计
	 * @param energyService
	 * @param lampService
	 * @param list
	 * @param opencloselamp
	 */
	public static void energyRecordhelp(EnergyService energyService,LampService lampService,List<Lamp> list,boolean opencloselamp) {
		//能耗开始
		for (Lamp lamp : list) {
			energyRecordhelp(energyService,lampService,lamp,opencloselamp);
		}
		//能耗结束
	}
	
}
