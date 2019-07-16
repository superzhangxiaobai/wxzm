package com.wf.ew.light.utils;

import java.util.ArrayList;
import java.util.List;

import com.wf.ew.common.PageResult;
import com.wf.ew.light.model.Lamp;
import com.wf.ew.light.service.EnergyService;
import com.wf.ew.light.service.LampService;
import com.wf.ew.light.service.OperateService;

/**
 * IP 直连工具类
 * @author Administrator
 *
 */
public class IPDirectUtil {

	/** 一键操作保存同一个IP和同一个通道的路灯 */
	public static List<String> ipList = new ArrayList<>();
	
	/**
	 * 一键开关灯IP直连
	 * @param addr
	 * @param lamp
	 * @param isOpenClose
	 * @param lampService
	 * @return
	 */
	public static ResponseResult directOneKeyHelp(String addr,Lamp lamp,boolean isOpenClose,LampService lampService) {
		/** 初始化返回值 */
		ResponseResult result = null;
		/** 读取IP和通道 */
		String ipChannel=lamp.getRemark().trim()+"-"+lamp.getChannel().trim();
		/** 如果同一个IP和同一个通道已经操作过了，直接跳过,修改数据库 */
		if (ipList.contains(ipChannel)) {
			result = new ResponseResult();
			result.setCode(200);
			result.setMsg("已经开灯或者关灯成功");
			OneKey.getInstance().addList(lamp);
			// 设置灯杆状态正常
			lamp.setRemark2(0);
			lampService.updateById(lamp);
			System.out.println("-----------------同一个IP和通道跳过");
			return result;
		}else {
			if (isOpenClose) {
				//一键开灯
				/** http请求 */
				/** IP直连打开连接,传递参数IP地址 */
				ResponseResult sendPostOpen = HttpClientUtil.sendPost(addr+"/open", lamp,true);
				/** 打开失败直接返回 */
				if (sendPostOpen.getCode() == 500 || sendPostOpen.getObj() == 0) {
					lamp.setLampWarn(1);
					lamp.setMemo3("开灯失败");
					OneKey.getInstance().addList(lamp);
					// 设置灯杆状态异常
					PoleUtil.updatePoleStateOneKey(lamp, lampService);
					System.out.println("-----------------------请求超时");
					/** 关闭连接，其实可以不用不关闭，打开失败了，就不存在关闭了 */
					ResponseResult sendPostClose = HttpClientUtil.sendPost(addr+"/close", lamp,true);
					return sendPostOpen;
				}
				ResponseResult sendPost = HttpClientUtil.sendPost(addr+"/ledopenorclose", lamp,true);
				ResponseResult sendPostGet = HttpClientUtil.sendPost(addr+"/getledchannelvalue", lamp,true);
				System.out.println("打印反馈======>"+sendPostGet.getObj());
				ResponseResult sendPostClose = HttpClientUtil.sendPost(addr+"/close", lamp,true);
				if (sendPostGet.getCode() == 500 ) {
					lamp.setLampWarn(1);
					lamp.setMemo3("开灯失败");
					OneKey.getInstance().addList(lamp);
					// 设置灯杆状态异常
					PoleUtil.updatePoleStateOneKey(lamp, lampService);
					System.out.println("-----------------------请求超时");
					return sendPostGet;
				}
				// 设置灯杆状态正常
				lamp.setRemark2(0);
				lampService.updateById(lamp);
				OneKey.getInstance().addList(lamp);
				ipList.add(ipChannel);
				result = sendPostGet;
			} else {
				//一键关灯
				ResponseResult sendPostOpen = HttpClientUtil.sendPost(addr+"/open", lamp,false);
				if (sendPostOpen.getCode() == 500 || sendPostOpen.getObj() == 0) {
					lamp.setLampWarn(1);
					lamp.setMemo3("关灯失败");
					OneKey.getInstance().addList(lamp);
					// 设置灯杆状态异常
					PoleUtil.updatePoleStateOneKey(lamp, lampService);
					System.out.println("-----------------------请求超时");
					ResponseResult sendPostClose = HttpClientUtil.sendPost(addr+"/close", lamp,false);
					return sendPostOpen;
				}
				
				ResponseResult sendPost = HttpClientUtil.sendPost(addr+"/ledopenorclose", lamp,false);
				ResponseResult sendPostGet = HttpClientUtil.sendPost(addr+"/getledchannelvalue", lamp,false);
				System.out.println("打印反馈======>"+sendPostGet.getObj());
				ResponseResult sendPostClose = HttpClientUtil.sendPost(addr+"/close", lamp,false);
				if (sendPostGet.getCode() == 500 ) {
					lamp.setLampWarn(1);
					lamp.setMemo3("关灯失败");
					OneKey.getInstance().addList(lamp);
					// 设置灯杆状态异常
					PoleUtil.updatePoleStateOneKey(lamp, lampService);
					System.out.println("-----------------------请求超时");
					return sendPostGet;
				}
				// 设置灯杆状态正常
				lamp.setRemark2(0);
				lampService.updateById(lamp);
				OneKey.getInstance().addList(lamp);
				ipList.add(ipChannel);
				result = sendPostGet;
			}
		}
		
		return result;
	}
	
	/**
	 * 单个灯开关IP直连
	 * @param addr
	 * @param lamp
	 * @param lampService
	 * @param energyService
	 * @param operateService
	 * @param nickName
	 * @return
	 */
	public static ResponseResult directUpdateHelp(String addr,Lamp lamp,LampService lampService,EnergyService energyService,OperateService operateService,String nickName) {
		ResponseResult result = null;
		/** 如果正在同步操作，中断同步操作 */
		if (LampOperateLock.getInstance().getMystatus() == 1) {
			// 正在同步
			LampOperateLock.getInstance().setStatus(1);
			System.out.println("正在同步操作，请等待一秒...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		LampOperateLock.getInstance().setStatus(1);
		if (lamp.getLampBefore() == null) {
			//关灯
			ResponseResult sendPostOpen = HttpClientUtil.sendPost(addr+"/open", lamp,false);
			if (sendPostOpen.getCode() == 500 || sendPostOpen.getObj() == 0) {
				sendPostOpen.setCode(500);
				ResponseResult sendPostClose = HttpClientUtil.sendPost(addr+"/close", lamp,false);
				return sendPostOpen;
			}
			ResponseResult sendPost = HttpClientUtil.sendPost(addr+"/ledopenorclose", lamp,false);
			ResponseResult sendPostGet = HttpClientUtil.sendPost(addr+"/getledchannelvalue", lamp,false);
			System.out.println("打印反馈======>"+sendPostGet.getObj());
			ResponseResult sendPostClose = HttpClientUtil.sendPost(addr+"/close", lamp,false);
			if (sendPostGet.getCode() == 500 ) {
				sendPostGet.setCode(500);
				return sendPostGet;
			}
			boolean ipb = false;
			if (lamp.getCategory().startsWith("路灯") || lamp.getCategory().startsWith("顶灯")) {
				// IP直连路灯、顶灯也是同城模式
				//PageResult<Lamp> glist = lampService.listLamp(1, 500, "group_ing", lamp.getGrouping());
				PageResult<Lamp> glist = lampService.listLamp(1, 500, "group_ing", lamp.getGrouping());
				List<Lamp> data = glist.getData();
				List<Lamp> sendData = new ArrayList<>() ;
				for (Lamp linshi : data) {
					linshi.setLampBefore(0);
					linshi.setLampAfter(0);
					linshi.setLampLeft(0);
					linshi.setLampRight(0);
					linshi.setCode("0000");
					// 设置灯杆状态为正常
					linshi.setRemark2(0);
					sendData.add(linshi);
					
				}
				ipb = lampService.updateBatch(sendData);
				//
				EnergyUtil.energyRecordhelp(energyService, lampService, sendData, false);
				OperateUtil.operateRecordhelp(operateService, lampService, lamp, false, nickName);
				
			} else {
				if (lamp.getSecondlevel() != null && lamp.getSecondlevel().startsWith("同城")) {
					System.out.println("------------------同城逻辑---------------------------");
					PageResult<Lamp> glist = lampService.listLamp(1, 500, "memo1", lamp.getMemo1());
					List<Lamp> data = glist.getData();
					List<Lamp> sendData = new ArrayList<>() ;
					for (Lamp linshi : data) {
						linshi.setLampBefore(0);
						linshi.setLampAfter(0);
						linshi.setLampLeft(0);
						linshi.setLampRight(0);
						linshi.setCode("0000");
						// 设置灯杆状态为正常
						linshi.setRemark2(0);
						sendData.add(linshi);
					}
					ipb = lampService.updateBatch(sendData);
					EnergyUtil.energyRecordhelp(energyService, lampService, sendData, false);
					OperateUtil.operateRecordhelp(operateService, lampService, lamp, false, nickName);
				} else {
					System.out.println("-----------------------非同城灯塔逻辑-------------------------------");
					lamp.setLampBefore(0);
					lamp.setLampAfter(0);
					lamp.setLampLeft(0);
					lamp.setLampRight(0);
					lamp.setCode("0000");
					// 设置灯杆状态为正常
					lamp.setRemark2(0);
					ipb=lampService.update(lamp);
					EnergyUtil.energyRecordhelp(energyService, lampService, lamp, false,0);//灯塔统计
					OperateUtil.operateRecordhelp(operateService, lampService, lamp, false, nickName);
				}
				
			}
			result = sendPostGet;
			//结束关灯
		} else {
			//开灯
			ResponseResult sendPostOpen = HttpClientUtil.sendPost(addr+"/open", lamp,true);
			if (sendPostOpen.getCode() == 500||sendPostOpen.getObj() == 0) {
				sendPostOpen.setCode(500);
				ResponseResult sendPostClose = HttpClientUtil.sendPost(addr+"/close", lamp,true);
				return sendPostOpen;
			}
			ResponseResult sendPost = HttpClientUtil.sendPost(addr+"/ledopenorclose", lamp,true);
			ResponseResult sendPostGet = HttpClientUtil.sendPost(addr+"/getledchannelvalue", lamp,true);
			System.out.println("打印反馈======>"+sendPostGet.getObj());
			ResponseResult sendPostClose = HttpClientUtil.sendPost(addr+"/close", lamp,true);
			if (sendPostGet.getCode() == 500 ) {
				sendPostGet.setCode(500);
				return sendPostGet;
			}
			boolean ipb = false;
			if (lamp.getCategory().startsWith("路灯") || lamp.getCategory().startsWith("顶灯")) {
				PageResult<Lamp> glist = lampService.listLamp(1, 500, "group_ing", lamp.getGrouping());
				List<Lamp> data = glist.getData();
				List<Lamp> sendData = new ArrayList<>() ;
				for (Lamp linshi : data) {
					linshi.setLampBefore(1);
					linshi.setLampAfter(1);
					linshi.setLampLeft(1);
					linshi.setLampRight(1);
					linshi.setCode("1111");
					// 设置灯杆状态为正常
					linshi.setRemark2(0);
					sendData.add(linshi);
					
				}
				ipb = lampService.updateBatch(sendData);
				//
				EnergyUtil.energyRecordhelp(energyService, lampService, sendData, true);
				OperateUtil.operateRecordhelp(operateService, lampService, lamp, true, nickName);
				
			} else {
				if (lamp.getSecondlevel() != null && lamp.getSecondlevel().startsWith("同城")) {
					System.out.println("------------------同城逻辑---------------------------");
					PageResult<Lamp> glist = lampService.listLamp(1, 500, "memo1", lamp.getMemo1());
					List<Lamp> data = glist.getData();
					List<Lamp> sendData = new ArrayList<>() ;
					for (Lamp linshi : data) {
						linshi.setLampBefore(1);
						linshi.setLampAfter(1);
						linshi.setLampLeft(1);
						linshi.setLampRight(1);
						linshi.setCode("1111");
						// 设置灯杆状态为正常
						linshi.setRemark2(0);
						sendData.add(linshi);
					}
					ipb = lampService.updateBatch(sendData);
					EnergyUtil.energyRecordhelp(energyService, lampService, sendData, true);
					OperateUtil.operateRecordhelp(operateService, lampService, lamp, true, nickName);
				} else {
					System.out.println("-----------------------非同城灯塔逻辑-------------------------------");
					lamp.setLampBefore(1);
					lamp.setLampAfter(1);
					lamp.setLampLeft(1);
					lamp.setLampRight(1);
					lamp.setCode("1111");
					// 设置灯杆状态为正常
					lamp.setRemark2(0);
					ipb=lampService.update(lamp);
					EnergyUtil.energyRecordhelp(energyService, lampService, lamp, true,0);//灯塔统计
					OperateUtil.operateRecordhelp(operateService, lampService, lamp, true, nickName);
				}
				
			}
			result = sendPostGet;
			//结束开灯
		}
		return result;
	}
	
	/**
	 * IP直连状态查询
	 * @param addr
	 * @param lamp
	 * @return
	 */
	public static ResponseResult directStateHelp(String addr,Lamp lamp) {
		ResponseResult result = null;
		ResponseResult sendPostOpen = HttpClientUtil.sendPost(addr+"/open", lamp,true);
		if (sendPostOpen.getCode() == 500 || sendPostOpen.getObj() == 0) {
			sendPostOpen.setCode(500);
			ResponseResult sendPostClose = HttpClientUtil.sendPost(addr+"/close", lamp,true);
			return sendPostOpen;
		}
		ResponseResult sendPostGet = HttpClientUtil.sendPost(addr+"/getledchannelvalue", lamp,true);
		if (sendPostGet.getCode() == 500 ) {
			sendPostGet.setCode(500);
			ResponseResult sendPostClose = HttpClientUtil.sendPost(addr+"/close", lamp,true);
			return sendPostGet;
		}
		ResponseResult sendPostClose = HttpClientUtil.sendPost(addr+"/close", lamp,true);
		return sendPostGet;
	}
	//end
}
