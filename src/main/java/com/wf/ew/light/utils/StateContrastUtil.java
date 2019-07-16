package com.wf.ew.light.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.wf.ew.common.JsonResult;
import com.wf.ew.common.PageResult;
import com.wf.ew.light.model.Lamp;
import com.wf.ew.light.service.EnergyService;
import com.wf.ew.light.service.LampService;
import com.wf.ew.light.service.OperateService;

/**
 * 路灯状态同步工具类
 * @author 
 *
 */
public class StateContrastUtil {

	public static void stateContrastHelp(LampService lampService,EnergyService energyService,OperateService operateService,String addr) {
		//EnergyUtil.energyRecordOneKeyhelp(energyService, lampService, OneKey.getInstance().getList(), true);
		//OperateUtil.operateRecordOneKeyhelp(operateService, true, getLoginUser().getNickName());
		// 添加操作状态
		//OperateState.getInstance().setState("have");
		// 键为路灯id,值为路灯对象
		Map<String, Lamp> single = new HashMap<>();
		// 键为组名称，值为本组的所有路灯
		Map<String, List<Lamp>> group = new HashMap<>();
		// 键为组名称，值为本组一个路灯对象
		Map<String, Lamp> groupSingle = new HashMap<>();
		// 十六进制对应的十进制位置
		String shiliu = "0123456789ABCDEF" ;
		try {
			LampOperateLock.getInstance().setMystatus(1);
			// 读取状态正常的路灯
			PageResult<Lamp> pr = lampService.listLamp(1, 500, "bucunzai", "bucunzai");
			// 路灯列表
	    	List<Lamp> list = pr.getData();
	    	// 循环路灯列表，为Map对象赋值
			for (Lamp lamp : list) {
				Lamp xinlight = new Lamp();
				BeanUtils.copyProperties(xinlight, lamp);
				single.put(lamp.getLampId()+"", xinlight);
				if (group.containsKey(lamp.getGrouping())) {
					group.get(lamp.getGrouping()).add(xinlight);
				} else {
					List<Lamp> gList = new ArrayList<>();
					gList.add(xinlight);
					group.put(lamp.getGrouping(), gList);
					groupSingle.put(lamp.getGrouping(), xinlight);
				}
			}
			// 循环路灯分组，一组对应一个模块，对每一个模块进行处理
			for (Map.Entry<String, Lamp> entry : groupSingle.entrySet()) { 
				/*if (entry.getValue().getCategory().startsWith("路灯") || entry.getValue().getCategory().startsWith("顶灯")) {
					continue;
				}*/
				if (LampOperateLock.getInstance().getStatus() == 1) {
					//正在进行其他操作，同步暂停，等下一次同步
					LampOperateLock.getInstance().setMystatus(0);
					System.out.println("===================系统正在操作==========================退出同步，等待下一次同步");
					break;
				}
				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				//判断是否是IP直连
				if (entry.getKey().startsWith("ip")) {
					System.out.println("--------------------IP直连，直接跳过查串口同步逻辑，走IP直连单独逻辑");
					System.out.println("-----------------------IP直连无需同步数据");
					Lamp directLamp = entry.getValue();
					List<Lamp> directList = group.get(entry.getKey());
					new Thread(new DirectThreadUtil(lampService, directLamp, directList,addr)).start();
					continue;
				}
				// 一组一个模块,根据地址查询一个模块的状态
				//JsonResult codeBefore = SerialPortUtil.serialPortHelpBefore(entry.getValue());
				JsonResult codeBefore = SerialPortUtil.serialPortHelpState(entry.getValue());
				System.out.println("分组:::"+entry.getKey()+"实时查询字符串==================================?????"+codeBefore);
				if (codeBefore.get("res").equals("请求超时")) {
					// 设置灯杆状态异常
					List<Lamp> error = group.get(entry.getKey());
					for (Lamp lamp : error) {
						PoleUtil.updatePoleStateOneKey(lamp, lampService);
					}
					// 如果查询之前状态超时，直接跳过本模块
					continue;
				}
				System.out.println("同步模块返回之前查询===================>"+codeBefore.get("res"));
				// 得到本模块之前状态字符串
				String tmpbefore = (String) codeBefore.get("res") ;
				// 截取字符串!0A 第三个字符
				//String state = tmpbefore.substring(2, 3) ;
				//>000D
				String state = tmpbefore.substring(4, 5) ;
				
				if (entry.getValue().getCategory()!=null&&entry.getValue().getCategory().startsWith("灯塔")) {
					// 灯塔分普通灯塔 和带路灯的灯塔
					if (entry.getValue().getSecondlevel()!=null&&entry.getValue().getSecondlevel().startsWith("路灯")) {
						// 带路灯的灯塔
						List<Lamp> list2 = group.get(entry.getKey());
						int localstate = 0 ;
						Map<String, Lamp> tmpgrouping = new HashMap<>();
						for (Lamp lamp : list2) {
							Lamp xinlight = new Lamp();
							BeanUtils.copyProperties(xinlight, lamp);
							tmpgrouping.put(lamp.getChannel().trim(), xinlight);
							tmpgrouping.put(lamp.getSecondchannel().trim(), xinlight);
							if (lamp.getLampBefore() == 1) {
								double pow = Math.pow(2, Integer.parseInt(lamp.getChannel())-1);
								localstate += (int)pow;
								
							}
							if (lamp.getLampSecond() == 1) {
								double pow = Math.pow(2, Integer.parseInt(lamp.getSecondchannel())-1);
								localstate += (int)pow;
								
							}
						}
						int remotestate = shiliu.indexOf(state);
						//取反
						remotestate = 15 - remotestate ;
						if (remotestate != localstate) {
							String remoteCode = channelHelp(remotestate);
							String localCode = channelHelp(localstate);
							if (remoteCode.charAt(0) != localCode.charAt(0)) {
								if (tmpgrouping.containsKey("1")) {
									Lamp lamp = tmpgrouping.get("1");
									if (lamp.getSecondchannel().equals("1")) {
										lamp.setLampSecond(Integer.parseInt(remoteCode.substring(0, 1)));
										lampService.updateById(lamp);
									} else {
										lamp.setLampBefore(Integer.parseInt(remoteCode.substring(0, 1)));
										lamp.setLampAfter(Integer.parseInt(remoteCode.substring(0, 1)));
										lamp.setLampLeft(Integer.parseInt(remoteCode.substring(0, 1)));
										lamp.setLampRight(Integer.parseInt(remoteCode.substring(0, 1)));
										lamp.setCode(remoteCode.substring(0, 1)+remoteCode.substring(0, 1)+remoteCode.substring(0, 1)+remoteCode.substring(0, 1));
										lampService.updateById(lamp);
									}
									
								}
							}
							if (remoteCode.charAt(1) != localCode.charAt(1)) {
								if (tmpgrouping.containsKey("2")) {
									Lamp lamp = tmpgrouping.get("2");
									if (lamp.getSecondchannel().equals("2")) {
										lamp.setLampSecond(Integer.parseInt(remoteCode.substring(1, 2)));
										lampService.updateById(lamp);
									} else {
										lamp.setLampBefore(Integer.parseInt(remoteCode.substring(1, 2)));
										lamp.setLampAfter(Integer.parseInt(remoteCode.substring(1, 2)));
										lamp.setLampLeft(Integer.parseInt(remoteCode.substring(1, 2)));
										lamp.setLampRight(Integer.parseInt(remoteCode.substring(1, 2)));
										lamp.setCode(remoteCode.substring(1, 2)+remoteCode.substring(1, 2)+remoteCode.substring(1, 2)+remoteCode.substring(1, 2));
										lampService.updateById(lamp);
									}
									
								}							
							}
							if (remoteCode.charAt(2) != localCode.charAt(2)) {
								if (tmpgrouping.containsKey("3")) {
									Lamp lamp = tmpgrouping.get("3");
									if (lamp.getSecondchannel().equals("3")) {
										lamp.setLampSecond(Integer.parseInt(remoteCode.substring(2, 3)));
										lampService.updateById(lamp);
									} else {
										lamp.setLampBefore(Integer.parseInt(remoteCode.substring(2, 3)));
										lamp.setLampAfter(Integer.parseInt(remoteCode.substring(2, 3)));
										lamp.setLampLeft(Integer.parseInt(remoteCode.substring(2, 3)));
										lamp.setLampRight(Integer.parseInt(remoteCode.substring(2, 3)));
										lamp.setCode(remoteCode.substring(2, 3)+remoteCode.substring(2, 3)+remoteCode.substring(2, 3)+remoteCode.substring(2, 3));
										lampService.updateById(lamp);
									}
									
								}	
							}
							if (remoteCode.charAt(3) != localCode.charAt(3)) {
								if (tmpgrouping.containsKey("4")) {
									Lamp lamp = tmpgrouping.get("4");
									if (lamp.getSecondchannel().equals("4")) {
										lamp.setLampSecond(Integer.parseInt(remoteCode.substring(3, 4)));
										lampService.updateById(lamp);
									} else {
										lamp.setLampBefore(Integer.parseInt(remoteCode.substring(3, 4)));
										lamp.setLampAfter(Integer.parseInt(remoteCode.substring(3, 4)));
										lamp.setLampLeft(Integer.parseInt(remoteCode.substring(3, 4)));
										lamp.setLampRight(Integer.parseInt(remoteCode.substring(3, 4)));
										lamp.setCode(remoteCode.substring(3, 4)+remoteCode.substring(3, 4)+remoteCode.substring(3, 4)+remoteCode.substring(3, 4));
										lampService.updateById(lamp);
									}
									
								}	
							}
						}
						//continue;
					} else {
						// 普通灯塔
						List<Lamp> list2 = group.get(entry.getKey());
						int localstate = 0 ;
						Map<String, Lamp> tmpgrouping = new HashMap<>();
						List<String> tmpList = new ArrayList<>();
						for (Lamp lamp : list2) {
							Lamp xinlight = new Lamp();
							BeanUtils.copyProperties(xinlight, lamp);
							tmpgrouping.put(lamp.getChannel().trim(), xinlight);
							if (lamp.getLampBefore() == 1 && !tmpList.contains(lamp.getChannel().trim())) {
								double pow = Math.pow(2, Integer.parseInt(lamp.getChannel())-1);
								localstate += (int)pow;
								tmpList.add(lamp.getChannel().trim());
							}
						}
						int remotestate = shiliu.indexOf(state);
						remotestate = 15 - remotestate ;
						if (remotestate != localstate) {
							String remoteCode = channelHelp(remotestate);
							String localCode = channelHelp(localstate);
							if (remoteCode.charAt(0) != localCode.charAt(0)) {
								if (tmpgrouping.containsKey("1")) {
									Lamp lamp = tmpgrouping.get("1");
									for (Lamp lamp2 : list2) {
										if (lamp2.getChannel().trim().equals("1")) {
											System.out.println(lamp2.getNickName()+"-------------1通道同步-----------------------");
											lamp2.setLampBefore(Integer.parseInt(remoteCode.substring(0, 1)));
											lamp2.setLampAfter(Integer.parseInt(remoteCode.substring(0, 1)));
											lamp2.setLampLeft(Integer.parseInt(remoteCode.substring(0, 1)));
											lamp2.setLampRight(Integer.parseInt(remoteCode.substring(0, 1)));
											lamp2.setCode(remoteCode.substring(0, 1)+remoteCode.substring(0, 1)+remoteCode.substring(0, 1)+remoteCode.substring(0, 1));
											lampService.updateById(lamp2);
										}
									}
									/*lamp.setLampBefore(Integer.parseInt(remoteCode.substring(0, 1)));
									lamp.setLampAfter(Integer.parseInt(remoteCode.substring(0, 1)));
									lamp.setLampLeft(Integer.parseInt(remoteCode.substring(0, 1)));
									lamp.setLampRight(Integer.parseInt(remoteCode.substring(0, 1)));
									lamp.setCode(remoteCode.substring(0, 1)+remoteCode.substring(0, 1)+remoteCode.substring(0, 1)+remoteCode.substring(0, 1));
									lampService.updateById(lamp);*/
								}
							}
							if (remoteCode.charAt(1) != localCode.charAt(1)) {
								if (tmpgrouping.containsKey("2")) {
									Lamp lamp = tmpgrouping.get("2");
									for(Lamp lamp2 : list2) {
										if (lamp2.getChannel().trim().equals("2")) {
											System.out.println(lamp2.getNickName()+"-------------2通道同步-----------------------");
											lamp2.setLampBefore(Integer.parseInt(remoteCode.substring(1, 2)));
											lamp2.setLampAfter(Integer.parseInt(remoteCode.substring(1, 2)));
											lamp2.setLampLeft(Integer.parseInt(remoteCode.substring(1, 2)));
											lamp2.setLampRight(Integer.parseInt(remoteCode.substring(1, 2)));
											lamp2.setCode(remoteCode.substring(1, 2)+remoteCode.substring(1, 2)+remoteCode.substring(1, 2)+remoteCode.substring(1, 2));
											lampService.updateById(lamp2);
										}
									}
									/*lamp.setLampBefore(Integer.parseInt(remoteCode.substring(1, 2)));
									lamp.setLampAfter(Integer.parseInt(remoteCode.substring(1, 2)));
									lamp.setLampLeft(Integer.parseInt(remoteCode.substring(1, 2)));
									lamp.setLampRight(Integer.parseInt(remoteCode.substring(1, 2)));
									lamp.setCode(remoteCode.substring(1, 2)+remoteCode.substring(1, 2)+remoteCode.substring(1, 2)+remoteCode.substring(1, 2));
									lampService.updateById(lamp);*/
								}
							}
							if (remoteCode.charAt(2) != localCode.charAt(2)) {
								if (tmpgrouping.containsKey("3")) {
									Lamp lamp = tmpgrouping.get("3");
									for(Lamp lamp2 : list2) {
										if (lamp2.getChannel().trim().equals("3")) {
											System.out.println(lamp2.getNickName()+"-------------3通道同步-----------------------");
											lamp2.setLampBefore(Integer.parseInt(remoteCode.substring(2, 3)));
											lamp2.setLampAfter(Integer.parseInt(remoteCode.substring(2, 3)));
											lamp2.setLampLeft(Integer.parseInt(remoteCode.substring(2, 3)));
											lamp2.setLampRight(Integer.parseInt(remoteCode.substring(2, 3)));
											lamp2.setCode(remoteCode.substring(2, 3)+remoteCode.substring(2, 3)+remoteCode.substring(2, 3)+remoteCode.substring(2, 3));
											lampService.updateById(lamp2);
										}
									}
									/*lamp.setLampBefore(Integer.parseInt(remoteCode.substring(2, 3)));
									lamp.setLampAfter(Integer.parseInt(remoteCode.substring(2, 3)));
									lamp.setLampLeft(Integer.parseInt(remoteCode.substring(2, 3)));
									lamp.setLampRight(Integer.parseInt(remoteCode.substring(2, 3)));
									lamp.setCode(remoteCode.substring(2, 3)+remoteCode.substring(2, 3)+remoteCode.substring(2, 3)+remoteCode.substring(2, 3));
									lampService.updateById(lamp);*/
								}
							}
							if (remoteCode.charAt(3) != localCode.charAt(3)) {
								if (tmpgrouping.containsKey("4")) {
									Lamp lamp = tmpgrouping.get("4");
									for(Lamp lamp2 : list2) {
										if (lamp2.getChannel().trim().equals("4")) {
											System.out.println(lamp2.getNickName()+"-------------4通道同步-----------------------");
											lamp2.setLampBefore(Integer.parseInt(remoteCode.substring(3, 4)));
											lamp2.setLampAfter(Integer.parseInt(remoteCode.substring(3, 4)));
											lamp2.setLampLeft(Integer.parseInt(remoteCode.substring(3, 4)));
											lamp2.setLampRight(Integer.parseInt(remoteCode.substring(3, 4)));
											lamp2.setCode(remoteCode.substring(3, 4)+remoteCode.substring(3, 4)+remoteCode.substring(3, 4)+remoteCode.substring(3, 4));
											lampService.updateById(lamp2);
										}
									}
									/*lamp.setLampBefore(Integer.parseInt(remoteCode.substring(3, 4)));
									lamp.setLampAfter(Integer.parseInt(remoteCode.substring(3, 4)));
									lamp.setLampLeft(Integer.parseInt(remoteCode.substring(3, 4)));
									lamp.setLampRight(Integer.parseInt(remoteCode.substring(3, 4)));
									lamp.setCode(remoteCode.substring(3, 4)+remoteCode.substring(3, 4)+remoteCode.substring(3, 4)+remoteCode.substring(3, 4));
									lampService.updateById(lamp);*/
								}
							}
						}
					}
				} else {
					
					// 路灯，顶灯，状态要么0，要么1
					if (state.equals("F")) {
						// 测试环境四个通道都可用
						//state = "1";
					}
					// 模块状态转换成整数
					int remotestate = shiliu.indexOf(state);
					remotestate = 15 - remotestate ;
					// 路灯和顶灯是一组同时操作，系统中路灯状态转换成整数
					int localstate = entry.getValue().getLampBefore();
					// 返回四个通道状态字符串
					String remoteCode = channelHelp(remotestate);
					String localCode = channelHelp(localstate);
					// 路灯顶灯只跟1通道相关,只需要比较模块跟系统的1通道即可
					if (remoteCode.charAt(0) != localCode.charAt(0)) {
						List<Lamp> list2 = group.get(entry.getKey());
						for (Lamp lamp : list2) {
							lamp.setLampBefore(Integer.parseInt(remoteCode.substring(0, 1)));
							lamp.setLampAfter(Integer.parseInt(remoteCode.substring(0, 1)));
							lamp.setLampLeft(Integer.parseInt(remoteCode.substring(0, 1)));
							lamp.setLampRight(Integer.parseInt(remoteCode.substring(0, 1)));
							lamp.setCode(remoteCode.substring(0, 1)+remoteCode.substring(0, 1)+remoteCode.substring(0, 1)+remoteCode.substring(0, 1));
							lampService.updateById(lamp);
						}
					}
					
				}
			}
		} catch (Exception e) {
			//捕获异常
		}finally {
			// 添加操作状态，同步状态之后，需要刷新页面
			OperateState.getInstance().setState("have");
			LampOperateLock.getInstance().setMystatus(0);
		}
		//end
	}
	
	public static double log(double value, double base) {
		return Math.log(value) / Math.log(base);
	}
	
	public static String channelHelp(int duishu) {
		if (duishu == 0) {//全关闭
			return "0000";
		}else if (duishu == 1) {//1通道
			return "1000";
		}else if (duishu == 2) {//2通道
			return "0100";
		}else if (duishu == 3) {//1,2通道
			return "1100";
		}else if (duishu == 4) {//3通道
			return "0010";
		}else if (duishu == 5) {//1,3通道
			return "1010";
		}else if (duishu == 6) {//2,3通道
			return "0110";
		}else if (duishu == 7) {//1,2,3通道
			return "1110";
		}else if (duishu == 8) {//4通道
			return "0001";
		}else if (duishu == 9) {//1,4通道
			return "1001";
		}else if (duishu == 10) {//2,4通道
			return "0101";
		}else if (duishu == 11) {//1,2,4,通道
			return "1101";
		}else if (duishu == 12) {//3,4通道
			return "0011";
		}else if (duishu == 13) {//1,3,4通道
			return "1011";
		}else if (duishu == 14) {//2,3,4通道
			return "0111";
		}else if (duishu == 15) {//1,2,3,4通道
			return "1111";
		}else {
			return "0000";
		}
	}
	
	public static String channelHelp(double duishu) {
		if (duishu == 0) {
			return "1000";
		}else if (duishu == 1) {
			return "0100";
		}else if (duishu>1.4 && duishu<1.6) {
			return "1100";
		}else if (duishu == 2) {
			return "0010";
		}else if (duishu>2.2 && duishu<2.4) {
			return "1010";
		}else if (duishu>2.4 && duishu <2.6) {
			return "0110";
		}else if (duishu>2.7 && duishu<2.9) {
			return "1110";
		}else if (duishu == 3) {
			return "0001";
		}else if (duishu > 3 && duishu < 3.2) {
			return "1001";
		}else if (duishu>3.3 && duishu<3.39) {
			return "0101";
		}else if (duishu>3.4 && duishu<3.5) {
			return "1101";
		}else if (duishu>3.5 && duishu<3.6) {
			return "0011";
		}else if (duishu>3.7 && duishu< 3.88) {
			return "0111";
		}else if (duishu>3.9) {
			return "1111";
		}else {
			return "0000";
		}
	}
	
	public static void main(String[] args) {
		//double log = log(14, 2);
		
		//1,2,3,4===>3.906
		//System.out.println("========================>"+log);
		//System.out.println("========================>"+Math.ceil(log));
		//System.out.println("========================>"+Math.ceil(3.0));
		
		System.out.println("-----------------");
		
	}
	
}
