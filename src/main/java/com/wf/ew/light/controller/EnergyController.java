package com.wf.ew.light.controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wf.ew.common.BaseController;
import com.wf.ew.common.JsonResult;
import com.wf.ew.common.PageResult;
import com.wf.ew.light.model.Energy;
import com.wf.ew.light.service.EnergyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Api(description = "能耗统计接口")
@Controller
@RequestMapping("/light/energy")
public class EnergyController extends BaseController {

	@Autowired
	private EnergyService energyService;
	
	@ApiIgnore
	@RequiresPermissions("energy:view")
	@RequestMapping
	public String energy (Model model) {
		return "light/statistics.html" ;
	}
	
	/**
     * 查询当天能耗
     **/
	@ApiIgnore
    @ResponseBody
    @RequestMapping("/currentEnergy")
    public JsonResult currentEnergy(Integer lampId, Integer state) {
    	// 已经去掉分页功能
       PageResult<Energy> listEnergy = energyService.listEnergy(1, 500, "date(update_time)", "curdate()");
       List<Energy> data = listEnergy.getData();
       double currentEnergy = 0.0;
       for (Energy energy : data) {
			Duration duration = Duration.between(energy.getCreateTime(),energy.getUpdateTime());
			
			long millis = duration.toMillis();//相差毫秒数
			
			double xh = millis/(1000*60*60.0);
			System.out.println("相差的计算小时为为======>"+xh);
			currentEnergy += xh*energy.getPower();
		}
        currentEnergy = currentEnergy / 1000.0 ;
        return JsonResult.ok().put("energy", String.format("%.6f", currentEnergy));
    }
    
    /**
     * 查询当月能耗
     **/
	@ApiIgnore
    @ResponseBody
    @RequestMapping("/monthEnergy")
    public JsonResult monthEnergy(Integer lampId, Integer state) {
    	// 已经去掉分页功能
       PageResult<Energy> listEnergy = energyService.listEnergy(1, 500, "DATE_FORMAT( update_time, '%Y%m' )", "month");
       List<Energy> data = listEnergy.getData();
       double currentEnergy = 0.0;
       for (Energy energy : data) {
			Duration duration = Duration.between(energy.getCreateTime(),energy.getUpdateTime());
			
			long millis = duration.toMillis();//相差毫秒数
			
			double xh = millis/(1000*60*60.0);
			System.out.println("相差的计算小时为为======>"+xh);
			currentEnergy += xh*energy.getPower();
		}
       currentEnergy = currentEnergy / 1000.0 ;
        return JsonResult.ok().put("energy", String.format("%.6f", currentEnergy));
    }
    
    /**
     * 累计能耗
     **/
	@ApiIgnore
    @ResponseBody
    @RequestMapping("/totalEnergy")
    public JsonResult totalEnergy(Integer lampId, Integer state) {
    	// 已经去掉分页功能
       PageResult<Energy> listEnergy = energyService.listEnergy(1, 500, "DATE_FORMAT( update_time, '%Y%m' )", "other");
       List<Energy> data = listEnergy.getData();
       double currentEnergy = 0.0;
       for (Energy energy : data) {
			Duration duration = Duration.between(energy.getCreateTime(),energy.getUpdateTime());
			
			long millis = duration.toMillis();//相差毫秒数
			
			double xh = millis/(1000*60*60.0);
			System.out.println("相差的计算小时为为======>"+xh);
			currentEnergy += xh*energy.getPower();
		}
       currentEnergy = currentEnergy / 1000.0 ;
        return JsonResult.ok().put("energy", String.format("%.6f", currentEnergy));
    }
    
    /**
     * 查询本周能耗报表
     **/
	@ApiIgnore
    @ResponseBody
    @RequestMapping("/weeklyEnergy")
    public JsonResult weeklyEnergy(Integer lampId, Integer state) {
       List<Double> allLamp = weeklyEnergyHelp(null);
       List<Double> luLamp = weeklyEnergyHelp("路灯");
       List<Double> taLamp = weeklyEnergyHelp("灯塔");
       return JsonResult.ok().put("energy", allLamp.toArray()).put("lighthouseenergy", taLamp.toArray()).put("lightenergy", luLamp.toArray());
    }
    
    /**
     * 本周能耗计算
     * @param type
     * @return
     */
    private List<Double> weeklyEnergyHelp(String type){
    	String sql = "" ;
    	if (type == null) {
			
		} else if(type != null && type.equals("路灯")) {
			sql = " AND remark = '路灯'" ;
		}else if (type != null && type.equals("灯塔")) {
			sql = " AND remark = '灯塔'" ;
		}
    	PageResult<Energy> listEnergyFirst = energyService.listEnergy(1, 500, "weekly", "(SELECT DATE_ADD(CONVERT(curdate(),CHAR(10)),INTERVAL - WEEKDAY(CONVERT(curdate(),CHAR(10)))+0 DAY))"+sql);
        PageResult<Energy> listEnergySecond = energyService.listEnergy(1, 500, "weekly", "(SELECT DATE_ADD(CONVERT(curdate(),CHAR(10)),INTERVAL - WEEKDAY(CONVERT(curdate(),CHAR(10)))+1 DAY))"+sql);
        PageResult<Energy> listEnergyThird = energyService.listEnergy(1, 500, "weekly", "(SELECT DATE_ADD(CONVERT(curdate(),CHAR(10)),INTERVAL - WEEKDAY(CONVERT(curdate(),CHAR(10)))+2 DAY))"+sql);
        PageResult<Energy> listEnergyFourth = energyService.listEnergy(1, 500, "weekly", "(SELECT DATE_ADD(CONVERT(curdate(),CHAR(10)),INTERVAL - WEEKDAY(CONVERT(curdate(),CHAR(10)))+3 DAY))"+sql);
        PageResult<Energy> listEnergyFive = energyService.listEnergy(1, 500, "weekly", "(SELECT DATE_ADD(CONVERT(curdate(),CHAR(10)),INTERVAL - WEEKDAY(CONVERT(curdate(),CHAR(10)))+4 DAY))"+sql);
        PageResult<Energy> listEnergySix = energyService.listEnergy(1, 500, "weekly", "(SELECT DATE_ADD(CONVERT(curdate(),CHAR(10)),INTERVAL - WEEKDAY(CONVERT(curdate(),CHAR(10)))+5 DAY))"+sql);
        PageResult<Energy> listEnergyLast = energyService.listEnergy(1, 500, "weekly", "(SELECT DATE_ADD(CONVERT(curdate(),CHAR(10)),INTERVAL - WEEKDAY(CONVERT(curdate(),CHAR(10)))+6 DAY))"+sql);
         
        double currentEnergyFirst = calHelp(listEnergyFirst.getData());
        double currentEnergySecond = calHelp(listEnergySecond.getData());
        double currentEnergyThird = calHelp(listEnergyThird.getData());
        double currentEnergyFourth = calHelp(listEnergyFourth.getData());
        double currentEnergyFive = calHelp(listEnergyFive.getData());
        double currentEnergySix = calHelp(listEnergySix.getData());
        double currentEnergyLast = calHelp(listEnergyLast.getData());
        double[] data = {currentEnergyFirst,currentEnergySecond,currentEnergyThird,currentEnergyFourth,currentEnergyFive,currentEnergySix,currentEnergyLast} ;
        List<Double> fdata = new ArrayList<>();
        for (double d : data) {
	  		double tmp = Double.parseDouble(String.format("%.6f", d));
	  		fdata.add(tmp);
	  	}
        return fdata; 
    }
    
    /**
     * 计算每天能耗
     * @param data
     * @return
     */
    private double calHelp(List<Energy> data) {
    	double currentEnergy = 0.0;
        for (Energy energy : data) {
 			Duration duration = Duration.between(energy.getCreateTime(),energy.getUpdateTime());
 			long hours = duration.toHours();
 			long minutes = duration.toMinutes();//相差的分钟数
 			long millis = duration.toMillis();//相差毫秒数
 			System.out.println("相差的小时为======>"+hours);
 			System.out.println("相差的分钟为======>"+minutes);
 			System.out.println("相差的毫秒为======>"+millis);
 			double xh = millis/(1000*60*60.0);
 			System.out.println("相差的计算小时为为======>"+xh);
 			currentEnergy += xh*energy.getPower();
 		}
        currentEnergy = currentEnergy / 1000.0 ;
    	return currentEnergy;
    }
    
    /**
     * 查询,按时间降序
     */
    @ResponseBody
    @RequestMapping("/list")
    @ApiOperation(value="照明统计",notes="照明统计", httpMethod = "GET")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "page", value = "第几页", required = true,dataType = "Int"),
    	@ApiImplicitParam(name = "limit", value = "每页多少条记录", required = true,dataType = "Int"),
        @ApiImplicitParam(name = "startDate", value = "起始日期", required = false,dataType = "String"),
        @ApiImplicitParam(name = "endDate", value = "结束日期", required = false,dataType = "String"),
        @ApiImplicitParam(name = "lampname", value = "路灯名称", required = false,dataType = "String")
    })
	public PageResult<Energy> list(Integer page, Integer limit, String startDate, String endDate, String lampname) {
    	
		return energyService.list(page, limit, startDate, endDate, lampname);
	}
    
    /**
     * 报表导出
     * @param startDate
     * @param endDate
     * @param lampname
     * @return
     */
    @ApiOperation(value="导出能耗报表",notes="自定义导出能耗报表", httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startDate", value = "起始日期", required = false,dataType = "String"),
        @ApiImplicitParam(name = "endDate", value = "结束日期", required = false,dataType = "String"),
        @ApiImplicitParam(name = "lampname", value = "路灯名称", required = false,dataType = "String")
    })
    @ResponseBody
    @RequestMapping("/report")
	public PageResult<Energy> list(String startDate, String endDate, String lampname) {
    	return energyService.report(startDate, endDate, lampname);
	}
    //end
	
}
