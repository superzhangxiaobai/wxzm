package com.wf.ew.light.controller;

import java.time.Duration;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wf.ew.common.BaseController;
import com.wf.ew.common.JsonResult;
import com.wf.ew.common.PageResult;
import com.wf.ew.common.utils.RedisUtil;
import com.wf.ew.light.model.Operate;
import com.wf.ew.light.service.OperateService;

import io.swagger.annotations.Api;

/**
 * 操作记录接口类
 * @author 
 *
 */

@Api(description="操作记录接口类")
@Controller
@RequestMapping("/light/operate")
public class OperateController extends BaseController {

	@Autowired
	private OperateService operateService;
	
	
	
	/**
     * 查询
     */
    /*@RequiresPermissions("config:view")*/
    @ResponseBody
    @RequestMapping("/list")
	public PageResult<Operate> list(Integer page, Integer limit, String searchKey, String searchValue) {
    	
		return operateService.listOperate(page, limit, searchKey, searchValue);
	}
    
    /**
     * 查询最新4条操作
     **/
    @ResponseBody
    @RequestMapping("/topOperate")
    public JsonResult topOperate(Integer lampId, Integer state) {
       PageResult<Operate> listOperate = operateService.listOperate(1, 4, null, null);;
       List<Operate> data = listOperate.getData();
       
       return JsonResult.ok().put("operate", data);
    }
    //end
	
}
