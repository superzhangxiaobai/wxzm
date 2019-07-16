package com.wf.ew.light.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wf.ew.common.JsonResult;
import com.wf.ew.common.PageResult;
import com.wf.ew.light.model.Lamp;
import com.wf.ew.light.model.LampApi;
import com.wf.ew.light.model.Operate;
import com.wf.ew.light.service.EnergyService;
import com.wf.ew.light.service.LampService;
import com.wf.ew.light.service.OperateService;
import com.wf.ew.light.utils.LampApiUtil;
import com.wf.ew.light.utils.LampOperateLock;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(description = "照明状态列表接口")
@RestController
@RequestMapping("/api/lamp")
public class LampApiController {

	@Autowired
	private LampService lampService ;
	
	@Autowired
	private EnergyService energyService ;
	
	@Autowired
	private OperateService operateService ;
	
	@Value("${lamp.direct.ip}")
	private String direct_ip;
	
	/*@CrossOrigin(origins = "http://192.168.101.88:8020", maxAge = 3600)*/
	@ApiOperation(value = "开灯关灯操作",notes="开灯和关灯操作",httpMethod = "POST")
	@ApiImplicitParams({
        /*@ApiImplicitParam(name = "startDate", value = "起始日期", required = false,dataType = "String"),*/
        @ApiImplicitParam(name = "state", value = "开灯：1，关灯：0", required = true,dataType = "Int",paramType = "query"),
        @ApiImplicitParam(name = "lampId", value = "路灯名称", required = true,paramType = "query",dataType = "Int")
    })
	@ResponseBody
    @RequestMapping("/operate")
	public JsonResult Operate(Integer lampId, Integer state,HttpServletResponse response) {
		System.out.println("----------------------- 调用成功--------------------------------------");
		System.out.println("lampId--------------"+lampId);
		System.out.println("state--------------"+state);
		response.setHeader("Access-Control-Allow-Origin", "*");
		if (LampOperateLock.getInstance().getStatus() == 1) {
			return JsonResult.error("其他人正在操作，请等待......");
		}
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
		Integer kaiguan = state == 1 ? state : null ;
		if (lampId>10000) {
			int id = lampId - 10000 ;
			Lamp byId = lampService.getById(id);
			if (byId != null) {
				/** 如果灯塔路灯状态和要修改的状态一样，提示重复操作 */
				if (byId.getLampSecond() == state) {
					return JsonResult.error("重复操作！请确认");
				}
				byId.setLampSecond(kaiguan);
				com.wf.ew.common.JsonResult update = LampApiUtil.update(byId, lampService, energyService, operateService,direct_ip);
				if (update.get("code").equals(200)) {
					return JsonResult.ok("路灯操作成功");
				} else {
					return JsonResult.error("路灯操作失败");
				}
				//return JsonResult.ok("路灯操作成功");
			} else {
				return JsonResult.error("路灯操作失败");
			}
		} else {
			int id = lampId ;
			Lamp byId = lampService.getById(id);
			if (byId != null) {
				if (byId.getLampBefore() == state) {
					return JsonResult.error("重复操作！请确认");
				}
				byId.setLampBefore(kaiguan);
				com.wf.ew.common.JsonResult update = LampApiUtil.update(byId, lampService, energyService, operateService,direct_ip);
				if (update.get("code").equals(200)) {
					return JsonResult.ok("路灯操作成功");
				} else {
					return JsonResult.error("路灯操作失败");
				}
				//return JsonResult.ok("路灯操作成功");
			} else {
				return JsonResult.error("路灯操作失败");
			}
		}
		//return null;
	}
	
	/*@ResponseBody
    @RequestMapping("/list")
	public PageResult<Lamp> list(Integer page, Integer limit, String searchKey, String searchValue) {
		return lampService.listLamp(1, 500, "bucunzai", "bucunzai");
	}*/
	
	@ApiOperation(value="灯塔状态列表",notes="灯塔25，灯塔13，路灯,点击Model，可以查看返回字段说明，默认显示 Model Schema 信息", 
			httpMethod = "GET",response = LampApi.class)
	@ApiResponses({
		@ApiResponse(code = 200, message = "请求失败"),
		@ApiResponse(code = 500, message = "请求失败")
	}) 
	@ResponseBody
    @RequestMapping("/list")
	public JsonResult list(HttpServletRequest request,HttpServletResponse response) {
		PageResult<Lamp> data = lampService.listLamp(1, 500, "bucunzai", "bucunzai");
		List<LampApi> mydata = new ArrayList<>();
		List<Lamp> lamps = data.getData();
		for (Lamp lamp : lamps) {
			
			if (lamp.getCategory()!=null&&lamp.getSecondlevel()!=null&&lamp.getCategory().startsWith("灯塔")&&lamp.getSecondlevel().startsWith("路灯")) {
				LampApi myLamp = new LampApi();
				myLamp.setLampId(lamp.getLampId());
				myLamp.setLampName(lamp.getNickName());
				myLamp.setLongitude(lamp.getLongitude());
				myLamp.setLatitude(lamp.getLatitude());
				Integer lampBefore = lamp.getLampBefore();
				String msg = lampBefore == 1 ? "开":"关";
				myLamp.setState(lamp.getLampBefore());
				lamp.getLampBefore();
				myLamp.setStateMessage(msg);
				myLamp.setPoleState(lamp.getRemark2());
				Integer remark2 = lamp.getRemark2();
				String msgPole = remark2 == 0 ? "正常":"异常";
				myLamp.setPoleMessage(msgPole);
				//myLamp.setCategory(lamp.getCategory());
				myLamp.setCategory(lamp.getMemo3());
				if (remark2 == -1) {
					myLamp.setState(2);
				}
				mydata.add(myLamp);
				
				LampApi myLampVice = new LampApi();
				myLampVice.setLampId(lamp.getLampId()+10000);
				myLampVice.setLampName(lamp.getNickName());
				myLampVice.setLongitude(lamp.getLongitude());
				myLampVice.setLatitude(lamp.getLatitude());
				Integer lampSecond = lamp.getLampSecond();
				String secondmsg = lampSecond == 1 ? "开":"关";
				myLampVice.setState(lamp.getLampSecond());
				myLampVice.setStateMessage(secondmsg);
				myLampVice.setPoleState(lamp.getRemark2());
				myLampVice.setPoleMessage(msgPole);
				myLampVice.setCategory("路灯");
				if (remark2 == -1) {
					myLampVice.setState(2);
				}
				mydata.add(myLampVice);
			} else {
				LampApi myLamp = new LampApi();
				myLamp.setLampId(lamp.getLampId());
				myLamp.setLampName(lamp.getNickName());
				myLamp.setLongitude(lamp.getLongitude());
				myLamp.setLatitude(lamp.getLatitude());
				Integer lampBefore = lamp.getLampBefore();
				String msg = lampBefore == 1 ? "开":"关";
				myLamp.setState(lamp.getLampBefore());
				lamp.getLampBefore();
				myLamp.setStateMessage(msg);
				myLamp.setPoleState(lamp.getRemark2());
				Integer remark2 = lamp.getRemark2();
				String msgPole = remark2 == 0 ? "正常":"异常";
				myLamp.setPoleMessage(msgPole);
				if (remark2 == -1) {
					myLamp.setState(2);
				}
				//myLamp.setCategory(lamp.getCategory());
				myLamp.setCategory(lamp.getMemo3());
				mydata.add(myLamp);
			}
			
		}
		
		return JsonResult.ok("查询成功").put("data", mydata) ;
	}
	
	@ApiOperation(value = "一键开灯关灯操作",notes="一键开灯和关灯操作",httpMethod = "POST")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "开灯：1，关灯：0", required = true,dataType = "Int",paramType = "query")
    })
	@ResponseBody
    @RequestMapping("/onekeyoperate")
	public JsonResult onekeyoperate(Integer id) {
		System.out.println("----------------------- 调用成功--------------------------------------");
		System.out.println("id--------------"+id);
		//判断其他人是否在一键操作
    	if (LampOperateLock.getInstance().getStatus() == 1) {
			return JsonResult.error("其他人正在一键操作，请等待...");
		}
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
		if (id==0) {
			LampApiUtil.openclose(id, lampService, energyService, operateService,direct_ip);
			return JsonResult.ok("一键关灯成功");
		} else if (id == 1) {
			LampApiUtil.openclose(id, lampService, energyService, operateService,direct_ip);
			return JsonResult.ok("一键开灯成功");
		}else {
			return JsonResult.error("无效的输入参数");
		} 
		//return null;
	}
	
}
