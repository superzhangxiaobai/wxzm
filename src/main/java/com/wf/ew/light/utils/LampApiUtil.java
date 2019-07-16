package com.wf.ew.light.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.beanutils.BeanUtils;

import com.wf.ew.common.JsonResult;
import com.wf.ew.common.PageResult;
import com.wf.ew.light.model.Lamp;
import com.wf.ew.light.service.EnergyService;
import com.wf.ew.light.service.LampService;
import com.wf.ew.light.service.OperateService;

/**
 * 外部系统操作工具类
 * @author Administrator
 *
 */
public class LampApiUtil {

	private static ReentrantLock lock = new ReentrantLock();
	
	/**
	 * 外部系统操作单个路灯操作
	 * @param lamp
	 * @param lampService
	 * @param energyService
	 * @param operateService
	 * @return
	 */
	public static JsonResult update (Lamp lamp,LampService lampService,EnergyService energyService,OperateService operateService,String addr) {
		//System.out.println("isOpenClose======>"+isOpenClose);
		// 判断是否为IP直连
		if (lamp.getGrouping()!=null&&lamp.getGrouping().startsWith("ip")) {
			ResponseResult rr = null;
			try {
				lock.lock();
				LampOperateLock.getInstance().setStatus(1);
				ResponseResult directUpdateHelp = IPDirectUtil.directUpdateHelp(addr,lamp, lampService, energyService, operateService,"华东电子");
				rr = directUpdateHelp;
			} finally {
				LampOperateLock.getInstance().setStatus(0);
				lock.unlock();
			}
			if (rr!=null&&rr.getCode()==200) {
				// 添加操作状态
				OperateState.getInstance().setState("have");
				return JsonResult.ok("修改成功").put("smsg", "IP直连操作");
			} else {
				return JsonResult.error("修改失败").put("smsg", "IP直连操作");
			}
		}
		/** 实时查询地址状态开始 */
		String autoHandFlag = "0000";
		try {
			lock.lock();
			JsonResult actualState = SerialPortUtil.serialPortHelpState(lamp);
			if (actualState.get("res").equals("请求超时")) {
				// 设置灯杆状态为异常
				PoleUtil.updatePoleState(lamp, lampService);
				// 如果查询之前状态超时，直接返回修改失败结果
				return JsonResult.error("修改失败").put("smsg", actualState.get("res"));
			}
			if (actualState.get("res").toString().startsWith(">")) {
				System.out.println("--------------------手动自动正确返回实时状态啦--------------------------===========");
				String realStateAutoHand = SerialPortUtil.realStateAutoHand(actualState.get("res").toString());
				autoHandFlag = realStateAutoHand;
			}
		} finally {
			lock.unlock();
		}
		/** 实时查询地址状态结束 */
		// 是否是路灯和顶灯标志，为null为路灯或者顶灯
		String isLudeng = null ;
		// 如果是灯塔，需要查询之前状态
		if (lamp.getCategory()!=null&&lamp.getCategory().startsWith("灯塔")) {
			try {
				//查询需要同步锁定，加锁
				lock.lock();
				//JsonResult codeBefore = serialPortHelpBefore(lamp);
				JsonResult codeBefore = SerialPortUtil.serialPortHelpBefore(lamp);
				if (codeBefore.get("res").equals("请求超时")) {
					// 如果查询之前状态超时，直接返回修改失败结果
					// 设置灯杆状态异常
					PoleUtil.updatePoleStateOneKey(lamp, lampService);
					return JsonResult.error("修改失败").put("smsg", codeBefore.get("res"));
				}
				System.out.println("返回之前查询===================>"+codeBefore.get("res"));
				// 得到之前状态字符串
				String tmpbefore = (String) codeBefore.get("res") ;
				// 截取字符串!0A 第三个字符
				isLudeng = tmpbefore.substring(2, 3) ;
			} finally {
				// 释放锁,不管代码执行是否正常，最后都要释放锁
				lock.unlock();
			}
			
		}
		
		
		//lock.lock();
		JsonResult code = null ;
		Lamp byId = lampService.getById(lamp.getLampId());
		// 灯塔的路灯 和 其他区别标记，0为其他，1为灯塔的路灯
		Integer codeType = 0 ;
		try {
			// 同步代码加锁
			lock.lock();
			if (byId.getSecondlevel() != null&&byId.getSecondlevel().startsWith("路灯")) {
				Integer tmpLampBefore = lamp.getLampBefore() == null ? 0 :lamp.getLampBefore() ;
				Integer tmpLampSecond = lamp.getLampSecond() == null ? 0 :lamp.getLampSecond() ;
				if (tmpLampBefore == byId.getLampBefore()) {
					//灯塔状态没变，开路灯
					char charAt = autoHandFlag.charAt(Integer.parseInt(lamp.getSecondchannel()) - 1);
					if (charAt == '1') {
						return JsonResult.error("修改失败").put("smsg", "此通道已为手动状态!");
					}
					//code = serialPortHelp(lamp,isLudeng,1);
					code = SerialPortUtil.serialPortHelp(lamp,isLudeng,1);
					codeType = 1 ;
				} else {
					//灯塔状态变了，开灯塔
					char charAt = autoHandFlag.charAt(Integer.parseInt(lamp.getChannel()) - 1);
					if (charAt == '1') {
						return JsonResult.error("修改失败").put("smsg", "此通道已为手动状态!");
					}
					//code = serialPortHelp(lamp,isLudeng,0);
					// 第三个参数为 null，正常发送命令，参数为0，代表开带路灯的灯塔，参数为1，代表开带路灯的灯塔的路灯
					code = SerialPortUtil.serialPortHelp(lamp,isLudeng,0);
					codeType = 0 ;
				}
			}else {
				//跟原来一模一样
				char charAt = autoHandFlag.charAt(Integer.parseInt(lamp.getChannel()) - 1);
				if (charAt == '1') {
					return JsonResult.error("修改失败").put("smsg", "此通道已为手动状态!");
				}
				//code = serialPortHelp(lamp,isLudeng,null);
				// 第二个参数：如果是普通灯塔，之前命令状态十六进制数，如果是路灯和顶灯，值为null,如果是带路灯的灯塔，值为十六进制数
				code = SerialPortUtil.serialPortHelp(lamp,isLudeng,null);
				codeType = 0 ;
			}
		} finally {
			// 不管代码执行是否有错，必须释放锁
			lock.unlock();
		}
		
		//JsonResult code = serialPortHelp(lamp,isLudeng,null);
		//lock.unlock();
		// 开灯还是关灯状态判断
		boolean opencloselamp = false ;
		if (lamp.getLampBefore() == null) {
			// 关灯塔，路灯，顶灯
			lamp.setLampBefore(0);
			lamp.setLampAfter(0);
			lamp.setLampLeft(0);
			lamp.setLampRight(0);
			if (codeType == 0) {
				opencloselamp = false ;
			}
		}else {
			// 开灯塔，路灯，顶灯
			lamp.setLampAfter(1);
			lamp.setLampLeft(1);
			lamp.setLampRight(1);
			if (codeType == 0) {
				opencloselamp = true ;
			}
		}
		if (lamp.getLampSecond() == null) {
			// 关灯塔的路灯
			// 顶灯，路灯，普通灯塔也走这条逻辑
			lamp.setLampSecond(0);
			if (codeType == 1) {
				opencloselamp = false ;
			}
		} else {
			// 开灯塔的路灯
			lamp.setLampSecond(1);
			if (codeType == 1) {
				opencloselamp = true ;
			}
		}
		/*if (lamp.getLampAfter() == null) {
			lamp.setLampAfter(0);
		}
		if (lamp.getLampLeft() == null) {
			lamp.setLampLeft(0);
		}
		if (lamp.getLampRight() == null) {
			lamp.setLampRight(0);
		}*/
		if (code.get("msg").equals("-1")) {
			return JsonResult.error("修改失败").put("smsg", code.get("res"));
		}
		if (codeType == 0) {
			lamp.setCode((String) code.get("msg"));
		}
		//lamp.setCode((String) code.get("msg"));
		if (code.get("res").equals("请求超时")) {
			// 设置灯杆状态异常
			PoleUtil.updatePoleStateOneKey(lamp, lampService);
			return JsonResult.error("修改失败").put("smsg", code.get("res"));
		}
		//PageResult<Lamp> list = lampService.listLamp(1, 500, searchKey, searchKey);
		boolean result = false ;
		if (lamp.getCategory().startsWith("路灯") || lamp.getCategory().startsWith("顶灯")) {
			PageResult<Lamp> glist = lampService.listLamp(1, 500, "group_ing", lamp.getGrouping());
			List<Lamp> data = glist.getData();
			List<Lamp> sendData = new ArrayList<>() ;
			for (Lamp linshi : data) {
				if (lamp.getLampBefore() == 0) {
					linshi.setLampBefore(0);
					linshi.setLampAfter(0);
					linshi.setLampLeft(0);
					linshi.setLampRight(0);
				}else {
					linshi.setLampAfter(1);
					linshi.setLampLeft(1);
					linshi.setLampRight(1);
					linshi.setLampBefore(1);
				}
				linshi.setCode((String) code.get("msg"));
				sendData.add(linshi);
				//lampService.update(linshi);
			}
			result = lampService.updateBatch(sendData);
			//
			EnergyUtil.energyRecordhelp(energyService, lampService, sendData, opencloselamp);
			OperateUtil.operateRecordhelp(operateService, lampService, lamp, opencloselamp, "华东电子");
			
			
		} else {
			if (lamp.getSecondlevel()!=null&&lamp.getSecondlevel().startsWith("同城")) {
				System.out.println("------------------同城逻辑---------------------------");
				PageResult<Lamp> glist = lampService.listLamp(1, 500, "memo1", lamp.getMemo1());
				List<Lamp> data = glist.getData();
				List<Lamp> sendData = new ArrayList<>() ;
				for (Lamp linshi : data) {
					if (lamp.getLampBefore() == 0) {
						linshi.setLampBefore(0);
						linshi.setLampAfter(0);
						linshi.setLampLeft(0);
						linshi.setLampRight(0);
					}else {
						linshi.setLampAfter(1);
						linshi.setLampLeft(1);
						linshi.setLampRight(1);
						linshi.setLampBefore(1);
					}
					linshi.setCode((String) code.get("msg"));
					sendData.add(linshi);
					//lampService.update(linshi);
				}
				result = lampService.updateBatch(sendData);
				EnergyUtil.energyRecordhelp(energyService, lampService, sendData, opencloselamp);
				//result=lampService.update(lamp);
				//EnergyUtil.energyRecordhelp(energyService, lampService, lamp, opencloselamp,codeType);
				OperateUtil.operateRecordhelp(operateService, lampService, lamp, opencloselamp, "华东电子");
			} else {
				System.out.println("-----------------------非同城灯塔逻辑-------------------------------");
				result=lampService.update(lamp);
				EnergyUtil.energyRecordhelp(energyService, lampService, lamp, opencloselamp,codeType);
				OperateUtil.operateRecordhelp(operateService, lampService, lamp, opencloselamp, "华东电子");
			}
			/*result=lampService.update(lamp);
			EnergyUtil.energyRecordhelp(energyService, lampService, lamp, opencloselamp,codeType);
			OperateUtil.operateRecordhelp(operateService, lampService, lamp, opencloselamp, "华东电子");*/
			
			
		}
		//能耗开始
		/*PageResult<Energy> listEnergy = energyService.listEnergy(1, 500, "lamp_id", lamp.getLampId()+"");
		List<Energy> data = listEnergy.getData();
		if (data.size()>0&&data.get(data.size()-1).getState()==0) {
			data.get(data.size()-1).setState(1);
			data.get(data.size()-1).setUpdateTime(null);
			energyService.update(data.get(data.size()-1));
		} else {
			Energy energy = new Energy() ;
			energy.setState(0);
			energy.setLampId(lamp.getLampId());
			Lamp byId2 = lampService.getById(lamp.getLampId());
			energy.setLampname(byId2.getNickName());
			energy.setPower(byId2.getPower());
			if (opencloselamp == true) {
				energyService.add(energy);
			}
			//energyService.add(energy);
		}*/
		//能耗结束
		//if (lampService.update(lamp)) {
		if (result) {
			// 添加操作状态
			OperateState.getInstance().setState("have");
			return JsonResult.ok("修改成功").put("smsg", code.get("res"));
		} else {
			return JsonResult.error("修改失败").put("smsg", code.get("res"));
		}
		//return JsonResult.ok();
	}
	
	/**
	 * 外部系统一键开灯或一键关灯
	 * @param id
	 * @param lampService
	 * @param energyService
	 * @param operateService
	 * @return
	 */
	public static JsonResult openclose(Integer id,LampService lampService,EnergyService energyService,OperateService operateService,String addr) {
    	
    	PageResult<Lamp> list = lampService.listLamp(1, 500, "bucunzai", "bucunzai");
    	List<Lamp> data = list.getData();
    	String oldAddr =null;
    	String oldCategory = null;
    	List<String> cList = new ArrayList<>();
    	List<String> eList = new ArrayList<>();
		if (list != null && data.size() > 0 ) {
			
    		if (id == 0) {
    			LampOperateLock.getInstance().setStatus(1);
				for (Lamp lamp : data) {
					
					lamp.setLampBefore(0);
					lamp.setLampAfter(0);
					lamp.setLampLeft(0);
					lamp.setLampRight(0);
					lamp.setLampSecond(0);
					lamp.setCode("0000");
					//判断是否是IP直连
					if (lamp.getGrouping()!=null&&lamp.getGrouping().startsWith("ip")) {
						IPDirectUtil.directOneKeyHelp(addr,lamp, false, lampService);
						continue;
					}
					Lamp lamp2 = new Lamp();
					try {
						BeanUtils.copyProperties(lamp2, lamp);
						
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}catch (Exception e) {
						
					}
					if (lamp.getMemo().trim().equals(oldAddr) || cList.contains(lamp.getMemo().trim())) {
						System.out.println("---------------同一个模块跳过");
						OneKey.getInstance().addList(lamp2);
						lampService.updateById(lamp);
						continue;
					}else {
						if (eList.contains(lamp.getMemo().trim())) {
							System.out.println("同一个模块-----------------------请求超时");
							// 设置灯杆状态异常
							PoleUtil.updatePoleStateOneKey(lamp, lampService);
							continue;
						}
						JsonResult result = SerialPortUtil.serialPortOneKeyHelp(lamp, false);
						if (result.get("res").equals("请求超时")) {
							System.out.println("-----------------------请求超时");
							eList.add(lamp.getMemo().trim());
							// 设置灯杆状态异常
							PoleUtil.updatePoleStateOneKey(lamp, lampService);
							continue;
						}
						lampService.updateById(lamp);
						OneKey.getInstance().addList(lamp2);
						oldAddr = lamp.getMemo().trim();
						cList.add(lamp.getMemo().trim());
					}
					
					//update(lamp);
				}
				EnergyUtil.energyRecordOneKeyhelp(energyService, lampService, OneKey.getInstance().getList(), false);
				OperateUtil.operateRecordOneKeyhelp(operateService, false, "华东电子");
				OneKey.getInstance().clear();
				LampOperateLock.getInstance().setStatus(0);
				IPDirectUtil.ipList.clear();
				// 添加操作状态
				OperateState.getInstance().setState("have");
				return JsonResult.ok("一键关灯成功");
			} else {
				LampOperateLock.getInstance().setStatus(1);
				for (Lamp lamp : data) {
					lamp.setLampBefore(1);
					lamp.setLampAfter(1);
					lamp.setLampLeft(1);
					lamp.setLampRight(1);
					lamp.setLampSecond(1);
					lamp.setCode("1111");
					// 判断是否是IP直连
					if (lamp.getGrouping()!=null&&lamp.getGrouping().startsWith("ip")) {
						IPDirectUtil.directOneKeyHelp(addr,lamp, true, lampService);
						continue;
					}
					Lamp lamp2 = new Lamp();
					try {
						BeanUtils.copyProperties(lamp2, lamp);
						//Thread.sleep(500);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}catch (Exception e) {
						
					}
					if (lamp.getMemo().trim().equals(oldAddr)) {
						System.out.println("---------------同一个模块跳过");
						OneKey.getInstance().addList(lamp2);
						lampService.updateById(lamp);
						continue;
					}else {
						if (eList.contains(lamp.getMemo().trim())) {
							System.out.println("同一个模块-------------------请求超时");
							// 设置灯杆状态异常
							PoleUtil.updatePoleStateOneKey(lamp, lampService);
							continue;
						}
						JsonResult result = SerialPortUtil.serialPortOneKeyHelp(lamp, true);
						if (result.get("res").equals("请求超时")) {
							lamp.setLampWarn(1);
							OneKey.getInstance().addList(lamp);
							System.out.println("-------------------请求超时");
							eList.add(lamp.getMemo().trim());
							// 设置灯杆状态异常
							PoleUtil.updatePoleStateOneKey(lamp, lampService);
							continue;
						}
						lampService.updateById(lamp);
						OneKey.getInstance().addList(lamp2);
						oldAddr = lamp.getMemo().trim();
					}
					
					//update(lamp);
				}
				EnergyUtil.energyRecordOneKeyhelp(energyService, lampService, OneKey.getInstance().getList(), true);
				OperateUtil.operateRecordOneKeyhelp(operateService, true, "华东电子");
				OneKey.getInstance().clear();
				LampOperateLock.getInstance().setStatus(0);
				IPDirectUtil.ipList.clear();
				// 添加操作状态
				OperateState.getInstance().setState("have");
				return JsonResult.ok("一键开灯成功");
			}
    		//return JsonResult.ok("存在");
		} else {
			return JsonResult.error("操作失败");
		}
    }
	//end
	
}
