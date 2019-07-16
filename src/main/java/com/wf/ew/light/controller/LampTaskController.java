package com.wf.ew.light.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wf.ew.common.BaseController;
import com.wf.ew.common.JsonResult;
import com.wf.ew.common.PageResult;
import com.wf.ew.common.exception.BusinessException;
import com.wf.ew.common.utils.ParamConfig;
import com.wf.ew.common.utils.SerialPortUtils;
import com.wf.ew.light.model.Energy;
import com.wf.ew.light.model.Lamp;
import com.wf.ew.light.model.LampTask;
import com.wf.ew.light.service.EnergyService;
import com.wf.ew.light.service.LampService;
import com.wf.ew.light.service.LampTaskService;
import com.wf.ew.light.service.OperateService;
import com.wf.ew.light.utils.EnergyUtil;
import com.wf.ew.light.utils.IPDirectUtil;
import com.wf.ew.light.utils.LampOperateLock;
import com.wf.ew.light.utils.OneKey;
import com.wf.ew.light.utils.OperateState;
import com.wf.ew.light.utils.OperateUtil;
import com.wf.ew.light.utils.PoleUtil;
import com.wf.ew.light.utils.SerialPortUtil;
import com.wf.ew.light.utils.StateContrastUtil;

import io.swagger.annotations.Api;

/**
 * 定时任务接口类
 * @author 
 *
 */

@Api(description="定时任务接口类")
@EnableScheduling
@Controller
@RequestMapping("/light/task")
public class LampTaskController extends BaseController implements  SchedulingConfigurer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LampTaskController.class) ;
	/**
	 * 开灯表达式
	 */
	private static String cron;
	/**
	 * 关灯表达式
	 */
	private static String closecron;
	/**
	 * 状态同步表达式
	 */
	private static String synccron;
	private static List<TriggerTask> triggerTaskList;
	private static ScheduledTaskRegistrar taskRegistrarglobal;
	
	@Value("${lamp.direct.ip}")
	private String direct_ip;
	
	@Autowired
	private LampTaskService lampTaskService ;
	
	@Autowired
	private LampService lampService ;
	
	@Autowired
	private EnergyService energyService ;
	
	@Autowired
	private OperateService operateService ;
	
	private static ReentrantLock lock = new ReentrantLock();
	
	/**
	 * 定时任务页面
	 * @param model
	 * @return
	 */
	@RequiresPermissions("task:view")
	@RequestMapping
	public String lampTask(Model model) {
		return "light/task.html" ;
	}
	
	/**
	 * 获取任务列表
	 * @param page
	 * @param limit
	 * @param searchKey
	 * @param searchValue
	 * @return
	 */
	@RequiresPermissions("task:view")
	@ResponseBody
	@RequestMapping("/list")
	public PageResult<LampTask> list (Integer page, Integer limit, String searchKey, String searchValue){
		return lampTaskService.listLampTask(page, limit, searchKey, searchValue);
	}
	
	/**
	 * 添加路灯任务
	 * @param lampTask
	 * @return
	 */
	@RequiresPermissions("task:update")
	@ResponseBody
	@RequestMapping("/add")
	public JsonResult add(LampTask lampTask) {
		lampTask.setState(0);
		if (lampTaskService.add(lampTask)) {
			return JsonResult.ok(200, "添加成功!");
		} else {
			return JsonResult.error(-1, "添加失败!");
		}
		
	}
	
	/**
	 * 修改路灯任务
	 * @param lampTask
	 * @return
	 */
	@RequiresPermissions("task:update")
	@ResponseBody
	@RequestMapping("/update")
	public JsonResult update(LampTask lampTask) {
		if (lampTaskService.update(lampTask)) {
			return JsonResult.ok(200, "修改成功!");
		} else {
			return JsonResult.error(-1, "修改失败!");
		}
	}
	
	/**
	 * 删除路灯任务
	 * @param lampTaskId
	 * @return
	 */
	@RequiresPermissions("task:update")
	@ResponseBody
	@RequestMapping("/delete")
	public JsonResult delete(Integer lampTaskId) {
		if (lampTaskId == null) {
			return JsonResult.error(-1, "参数lampTaskId不能为空");
		}
		if (lampTaskService.removeById(lampTaskId)) {
			return JsonResult.ok(200, "删除成功");
		} else {
			return JsonResult.error(-1, "删除失败");
		}
	}
	
	/**
	 * 修改路灯任务状态
	 * @param lampTaskId
	 * @param state
	 * @return
	 */
	@RequiresPermissions("task:update")
	@ResponseBody
	@RequestMapping("/updateState")
	public JsonResult updateState (Integer lampTaskId ,Integer state) {
		if (lampTaskId == null) {
			return JsonResult.error(-1, "参数lampTaskId不能为空");
		}
		if (state == null || (state != 0 && state != 1)) {
            return JsonResult.error(-1,"状态值不正确");
        }
		LampTask lampTask = new LampTask();
		lampTask.setTaskId(lampTaskId);
		lampTask.setState(state);
		if (lampTaskService.updateById(lampTask)) {
			return JsonResult.ok();
		} else {
			return JsonResult.error();
		}
	}
	
	/**
	 * 修改路灯任务状态,立即执行
	 * @param taskId
	 * @return
	 */
	@RequiresPermissions("task:update")
	@ResponseBody
	@RequestMapping("/execTask")
	public JsonResult execTask (Integer taskId ) {
		if (taskId == null) {
			return JsonResult.error(-1, "参数taskId不能为空");
		}
		for (TriggerTask triggerTask : triggerTaskList) {
			taskRegistrarglobal.scheduleTriggerTask(triggerTask);
		}
		
		return JsonResult.ok();
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		//开关灯任务控制
		LampTask byId = lampTaskService.getByTaskType(3);
		//同步状态控制
		LampTask byId2 = lampTaskService.getByTaskType(4);
		if (byId != null && byId.getOpencron() != null && byId.getOpencron().startsWith("open")) {
			System.out.println("======================一键开关灯任务开启====================================");
			taskRegistrar.addTriggerTask(doTask(), getTrigger());
			taskRegistrar.addTriggerTask(doCloseTask(), getCloseTrigger());
		} else {
			System.out.println("=========================一键开关灯任务关闭===========================");
		}
		//所有灯开灯任务
		//taskRegistrar.addTriggerTask(doTask(), getTrigger());
		//所有灯关灯任务
		//taskRegistrar.addTriggerTask(doCloseTask(), getCloseTrigger());
		//路灯状态同步任务
		if (byId2 != null && byId2.getOpencron() != null && byId2.getOpencron().startsWith("open")) {
			System.out.println("=====================同步状态任务开启================================");
			taskRegistrar.addTriggerTask(doSyncTask(), getSyncTrigger());
		}else {
			System.out.println("=================同步状态任务关闭===================");
		}
		//taskRegistrar.addTriggerTask(doSyncTask(), getSyncTrigger());
		triggerTaskList = taskRegistrar.getTriggerTaskList();
		taskRegistrarglobal = taskRegistrar ;
	}
	
	/**
	 * 开灯任务
	 * @return
	 */
	private Runnable doTask() {
		return new Runnable() {
			
			@Override
			public void run() {
				// 业务逻辑
				System.out.println("执行了LampTaskController,时间为:" + new Date(System.currentTimeMillis()));
				if (LampOperateLock.getInstance().getMystatus() == 1) {
					// 正在同步
					LampOperateLock.getInstance().setStatus(1);
					System.out.println("正在同步操作，请等待一秒...");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}finally {
						LampOperateLock.getInstance().setStatus(0);
					}
				}
				// 一键开灯逻辑
				PageResult<Lamp> list = lampService.listLamp(1, 500, "bucunzai", "bucunzai");
		    	List<Lamp> data = list.getData();
		    	String oldAddr =null;
		    	List<String> cList = new ArrayList<>();
		    	List<String> eList = new ArrayList<>();
		    	if (list != null && data.size() > 0 ) {
		    		for (Lamp lamp : data) {
						lamp.setLampBefore(1);
						lamp.setLampAfter(1);
						lamp.setLampLeft(1);
						lamp.setLampRight(1);
						lamp.setLampSecond(1);
						lamp.setCode("1111");
						//判断是否是IP直连
						if (lamp.getGrouping()!=null&&lamp.getGrouping().startsWith("ip")) {
							IPDirectUtil.directOneKeyHelp(direct_ip,lamp, true, lampService);
							continue;
						}
						if (lamp.getMemo().trim().equals(oldAddr) || cList.contains(lamp.getMemo().trim())) {
							System.out.println("---------------同一个模块跳过");
							lampService.updateById(lamp);
							OneKey.getInstance().addList(lamp);
							continue;
						}else {
							if (eList.contains(lamp.getMemo().trim())) {
								// 设置灯杆状态异常
								PoleUtil.updatePoleStateOneKey(lamp, lampService);
								System.out.println("同一个模块-------------------请求超时");
								continue;
							}
							JsonResult result = SerialPortUtil.serialPortOneKeyHelp(lamp, true);
							if (result.get("res").equals("请求超时")) {
								// 设置灯杆状态异常
								PoleUtil.updatePoleStateOneKey(lamp, lampService);
								eList.add(lamp.getMemo().trim());
								System.out.println("-------------------请求超时");
								continue;
							}
							lampService.updateById(lamp);
							OneKey.getInstance().addList(lamp);
							oldAddr = lamp.getMemo().trim();
							cList.add(lamp.getMemo().trim());
						}
						
						//update(lamp);
					}
					EnergyUtil.energyRecordOneKeyhelp(energyService, lampService, OneKey.getInstance().getList(), true);
					OneKey.getInstance().clear();
					IPDirectUtil.ipList.clear();
					// 添加操作状态
					OperateState.getInstance().setState("have");
		    	}
				/*Lamp byId = lampService.getById(30);
				if (byId.getLampBefore() == 0) {
					// 添加操作状态
					OperateState.getInstance().setState("have");
					byId.setLampBefore(1);
					updateLamp(byId);
				}*/
				
			}
		};
	}
	
	/**
	 * 关灯任务
	 * @return
	 */
	private Runnable doCloseTask() {
		return new Runnable() {
			
			@Override
			public void run() {
				// 业务逻辑
				System.out.println("执行了LampTaskController,时间为:" + new Date(System.currentTimeMillis()));
				if (LampOperateLock.getInstance().getMystatus() == 1) {
					// 正在同步
					LampOperateLock.getInstance().setStatus(1);
					System.out.println("正在同步操作，请等待一秒...");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}finally {
						LampOperateLock.getInstance().setStatus(0);
					}
				}
				// 一键关灯逻辑
				PageResult<Lamp> list = lampService.listLamp(1, 500, "bucunzai", "bucunzai");
		    	List<Lamp> data = list.getData();
		    	String oldAddr =null;
		    	List<String> cList = new ArrayList<>();
		    	List<String> eList = new ArrayList<>();
		    	if (list != null && data.size() > 0 ) {
		    		for (Lamp lamp : data) {
						
						lamp.setLampBefore(0);
						lamp.setLampAfter(0);
						lamp.setLampLeft(0);
						lamp.setLampRight(0);
						lamp.setLampSecond(0);
						lamp.setCode("0000");
						//判断是否是IP直连
						if (lamp.getGrouping()!=null&&lamp.getGrouping().startsWith("ip")) {
							IPDirectUtil.directOneKeyHelp(direct_ip,lamp, false, lampService);
							continue;
						}
						if (lamp.getMemo().trim().equals(oldAddr) || cList.contains(lamp.getMemo().trim())) {
							System.out.println("---------------同一个模块跳过");
							lampService.updateById(lamp);
							OneKey.getInstance().addList(lamp);
							continue;
						}else {
							if (eList.contains(lamp.getMemo().trim())) {
								// 设置灯杆状态异常
								PoleUtil.updatePoleStateOneKey(lamp, lampService);
								System.out.println("同一个模块-----------------------请求超时");
								continue;
							}
							JsonResult result = SerialPortUtil.serialPortOneKeyHelp(lamp, false);
							if (result.get("res").equals("请求超时")) {
								System.out.println("-----------------------请求超时");
								// 设置灯杆状态异常
								PoleUtil.updatePoleStateOneKey(lamp, lampService);
								eList.add(lamp.getMemo().trim());
								continue;
							}
							lampService.updateById(lamp);
							OneKey.getInstance().addList(lamp);
							oldAddr = lamp.getMemo().trim();
							cList.add(lamp.getMemo().trim());
						}
						
						//update(lamp);
					}
					EnergyUtil.energyRecordOneKeyhelp(energyService, lampService, OneKey.getInstance().getList(), false);
					OneKey.getInstance().clear();
					IPDirectUtil.ipList.clear();
					// 添加操作状态
					OperateState.getInstance().setState("have");
		    	}
				/*Lamp byId = lampService.getById(30);
				if (byId.getLampBefore() == 1) {
					// 添加操作状态
					OperateState.getInstance().setState("have");
					byId.setLampBefore(null);
					updateLamp(byId);
				}*/
				
			}
		};
	}
	
	/**
	 * 路灯状态同步
	 * @return
	 */
	private Runnable doSyncTask() {
		return new Runnable() {
			
			@Override
			public void run() {
				// 业务逻辑
				System.out.println("执行了LampTaskController,时间为:" + new Date(System.currentTimeMillis()));
				System.out.println("每隔三分钟同步一次路灯状态..........");
				// 路灯状态同步
				if (LampOperateLock.getInstance().getStatus() == 0) {
		    		System.out.println("------------------每隔三分钟同步一次状态--------------------");
		        	
		        	StateContrastUtil stateContrastUtil = new StateContrastUtil();
		        	stateContrastUtil.stateContrastHelp(lampService, energyService, operateService,direct_ip);
				}else {
					System.out.println("------------------正在一键操作，本次同步取消。--------------------");
				}
				
			}
		};
	}
	
	private Trigger getTrigger() {
		return new Trigger() {
			
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				//开灯触发器
				CronTrigger trigger = new CronTrigger(getCron()) ;
				return trigger.nextExecutionTime(triggerContext);
			}
		};
	}
	
	private Trigger getCloseTrigger() {
		return new Trigger() {
			
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				//关灯触发器
				CronTrigger trigger = new CronTrigger(getCloseCron()) ;
				return trigger.nextExecutionTime(triggerContext);
			}
		};
	}
	
	private Trigger getSyncTrigger() {
		return new Trigger() {
			
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				//状态同步触发器
				CronTrigger trigger = new CronTrigger(getSyncCron()) ;
				return trigger.nextExecutionTime(triggerContext);
			}
		};
	}
	
	/**
	 * 获取关灯表达式
	 */
	public String getCloseCron() {
        String newCron = "0 40 16 * * ?";
        //LampTask byId = lampTaskService.getById(5);
        LampTask byId = lampTaskService.getByTaskType(1);
        if (byId != null) {
        	 newCron = byId.getClosecron();
		}
       
        System.out.println("-----------------------什么时候获取关灯定时任务--------------------");
        if (StringUtils.isEmpty(newCron)) {
        	throw new BusinessException("The config cron expression is empty");
        }
        if (!newCron.equals(closecron)) {
        	LOGGER.info(new StringBuffer("Cron has been changed to:'").append(newCron).append("'. Old cron was:'").append(cron).append("'").toString());
        	closecron = newCron;
        }
        return closecron;
    }
	
	/**
	 * 获取开灯表达式
	 */
	public String getCron() {
        String newCron = "0 40 16 * * ?";
        //LampTask byId = lampTaskService.getById(5);
        LampTask byId = lampTaskService.getByTaskType(1);
        if (byId != null) {
        	 newCron = byId.getOpencron();
		}
       
        System.out.println("-----------------------什么时候获取开灯定时任务--------------------");
        if (StringUtils.isEmpty(newCron)) {
        	throw new BusinessException("The config cron expression is empty");
        }
        if (!newCron.equals(cron)) {
        	LOGGER.info(new StringBuffer("Cron has been changed to:'").append(newCron).append("'. Old cron was:'").append(cron).append("'").toString());
            cron = newCron;
        }
        return cron;
    }
	
	/**
	 * 获取状态同步表达式
	 */
	public String getSyncCron() {
        String newCron = "0 */3 * * * ?";
        //LampTask byId = lampTaskService.getById(5);
        LampTask byId = lampTaskService.getByTaskType(2);
        if (byId != null) {
        	 newCron = byId.getOpencron();
		}
       
        System.out.println("-----------------------什么时候获取同步状态定时任务--------------------");
        if (StringUtils.isEmpty(newCron)) {
        	throw new BusinessException("The config cron expression is empty");
        }
        if (!newCron.equals(synccron)) {
        	LOGGER.info(new StringBuffer("Cron has been changed to:'").append(newCron).append("'. Old cron was:'").append(cron).append("'").toString());
        	synccron = newCron;
        }
        return synccron;
    }
	
	
	
	/**
	 * 修改路灯状态
	 
	 * @return
	 */
	@RequiresPermissions("task:update")
	@ResponseBody
	@RequestMapping("/updateLamp")
	public JsonResult updateLamp (Lamp lamp) {
		// 是否是路灯和顶灯标志，为null为路灯或者顶灯
		String isLudeng = null ;
		// 如果是灯塔，需要查询之前状态
		if (lamp.getCategory().startsWith("灯塔")) {
			//JsonResult codeBefore = serialPortHelpBefore(lamp);
			try {
				//查询需要同步锁定，加锁
				lock.lock();
				JsonResult codeBefore = SerialPortUtil.serialPortHelpBefore(lamp);
				if (codeBefore.get("res").equals("请求超时")) {
					// 如果查询之前状态超时，直接返回修改失败结果
					return JsonResult.error("修改失败").put("smsg", codeBefore.get("res"));
				}
				System.out.println("返回之前查询===================>"+codeBefore.get("res"));
				// 得到之前状态字符串
				String tmpbefore = (String) codeBefore.get("res") ;
				// 截取字符串!0A 第二个字符
				isLudeng = tmpbefore.substring(2, 3) ;
			} finally {
				// 释放锁,不管代码执行是否正常，最后都要释放锁
				lock.unlock();
			}
			
			// 释放锁
			//lock.unlock();
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
					//灯塔状态没变，开灯塔的路灯
					//code = serialPortHelp(lamp,isLudeng,1);
					code = SerialPortUtil.serialPortHelp(lamp,isLudeng,1);
					codeType = 1 ;
				} else {
					//灯塔状态变了，开灯塔
					// 第三个参数为 null，正常发送命令，参数为0，代表开带路灯的灯塔，参数为1，代表开带路灯的灯塔的路灯
					code = SerialPortUtil.serialPortHelp(lamp,isLudeng,0);
					codeType = 0 ;
				}
			}else {
				//跟原来一模一样
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
		
		if (code.get("msg").equals("-1")) {
			return JsonResult.error("修改失败").put("smsg", code.get("res"));
		}
		if (codeType == 0) {
			// 不是灯塔的路灯才设置
			lamp.setCode((String) code.get("msg"));
		}
		//lamp.setCode((String) code.get("msg"));
		if (code.get("res").equals("请求超时")) {
			return JsonResult.error("修改失败").put("smsg", code.get("res"));
		}
		//PageResult<Lamp> list = lampService.listLamp(1, 500, searchKey, searchKey);
		boolean result = false ;
		if (lamp.getCategory().startsWith("路灯") || lamp.getCategory().startsWith("顶灯")) {
			PageResult<Lamp> glist = lampService.listLamp(1, 500, "grouping", lamp.getGrouping());
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
			result=lampService.updateBatch(sendData);
			EnergyUtil.energyRecordhelp(energyService, lampService, sendData, opencloselamp);
		} else {
			result=lampService.update(lamp);
			EnergyUtil.energyRecordhelp(energyService, lampService, lamp, opencloselamp,codeType);
			
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
			return JsonResult.ok("修改成功").put("smsg", code.get("res"));
		} else {
			return JsonResult.error("修改失败").put("smsg", code.get("res"));
		}
		//return JsonResult.ok();
	}
	
	
	//end
}
