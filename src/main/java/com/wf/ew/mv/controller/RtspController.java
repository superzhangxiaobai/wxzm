package com.wf.ew.mv.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wf.ew.common.BaseController;
import com.wf.ew.common.JsonResult;
import com.wf.ew.common.PageResult;
import com.wf.ew.common.exception.BusinessException;
import com.wf.ew.common.utils.Kernel32;
import com.wf.ew.common.utils.RunUtil;
import com.wf.ew.mv.model.Rtsp;
import com.wf.ew.mv.service.RtspService;

/**
 * 摄像头视频管理
 * @author null
 *
 */
@Controller
@RequestMapping("/mv/rtsp")
public class RtspController extends BaseController {

	@Autowired
	private RtspService rtspService ;
	
	@RequiresPermissions("rtsp:view")
	@RequestMapping
	public String rtsp (Model model) {
		return "mv/rtsp.html" ;
	}
	
	@RequiresPermissions("rtsp:view")
	@ResponseBody
	@RequestMapping("/list")
	public PageResult<Rtsp> list (Integer page, Integer limit, String searchKey, String searchValue){
		return rtspService.listRtsp(page, limit, searchKey, searchValue);
	}
	
	/**
	 * 推流验证
	 * @param rtsp
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping(value="/rtmp/startPush",method= {RequestMethod.POST})
	public JsonResult startPush(String rtspId,HttpServletRequest request,HttpServletResponse response) {
		System.out.println("-------------------start推流开始回调");
		Integer id = Integer.parseInt(rtspId);
		Rtsp rtsp = new Rtsp();
		rtsp.setRtspId(id);
		rtsp.setState(1);
		if (rtspService.updateById(rtsp)) {
			return JsonResult.ok();
		} else {
			return JsonResult.error();
		}
		/*if (null != password && password.equals("123456")) {
			return JsonResult.ok();
		} else {
			//return JsonResult.error();
			//throw new BusinessException("HTTP/1.0 404 Not Found");
			return JsonResult.error("{\\\"code\\\":\\\"500\\\",\\\"detail\\\":\\\"SUCCESS\\");
		}*/
		//return JsonResult.ok();
	}
	
	/**
	 * 推流操作
	 * @param start
	 * @return
	 */
	@RequiresPermissions("rtsp:update")
	@ResponseBody
	@RequestMapping("/push")
	public JsonResult pushRtmp(Integer rtspId) {
		System.out.println("------------------开始推流rtmp");
		if (rtspId == null) {
            return JsonResult.error("参数rtspId不能为空");
        }
		Runtime runtime = Runtime.getRuntime();
		try {
			//Process process = runtime.exec("cmd /k start dir");//会打开一个新窗口后执行dir指令，原窗口不会封闭。
			//cmd /c dir 是执行完dir命令后封闭命令窗口。
			//cmd /c start dir 会打开一个新窗口后执行dir指令，原窗口会封闭。
			//cmd /k dir 是执行完dir命令后不封闭命令窗口。
			//cmd /k start dir 会打开一个新窗口后执行dir指令，原窗口不会封闭。
			
			//Process process = runtime.exec("cmd /c start ping 127.0.0.1");//会打开一个新窗口后执行dir指令，原窗口会封闭。
			Rtsp rm = rtspService.getById(rtspId);
			String path = rm.getCmdpath()+"/" ;
			String cmd = "ffmpeg -i \""+rm.getVideourl()+"\" -keyint_min 12 -g 12 -sc_threshold 0 -vcodec h264 -acodec aac -f flv \""+rm.getRtmpurl()+"\"?rtspId="+rtspId;
			cmd = path + cmd ;
			Process process = runtime.exec("cmd /k start "+cmd+"");//会打开一个新窗口后执行dir指令，原窗口会封闭。
			//Process process = runtime.exec(cmd);//会打开一个新窗口后执行dir指令，原窗口会封闭。
			//Process process2 = runtime.exec("cmd /c start ping 127.0.0.1");
			//new Thread(new RunUtil(process2)).start();
		
			
			
			long pid = -1 ;
			Field field = null ;
			field = process.getClass().getDeclaredField("handle");
			field.setAccessible(true);
			pid = Kernel32.INSTANCE.GetProcessId((Long) field.get(process));
			Rtsp rtsp = new Rtsp();
			rtsp.setRtspId(rtspId);
			rtsp.setPid((int) pid);
			rtsp.setState(1);
			rtspService.updateById(rtsp);
			System.out.println("本次启动的进程id是========"+pid);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return JsonResult.ok() ;
	}
	
	
	/**
	 * 停止推流操作
	 * @param start
	 * @return
	 */
	@RequiresPermissions("rtsp:update")
	@ResponseBody
	@RequestMapping("/stop")
	public JsonResult stopRtmp(Integer rtspId) {
		System.out.println("------------------停止推流rtmp");
		if (rtspId == null) {
            return JsonResult.error("参数rtspId不能为空");
        }
		Runtime runtime = Runtime.getRuntime();
		try {
			
			Rtsp rm = rtspService.getById(rtspId);
			//taskkill /pid 14396  -t  -f
			String result ="cmd /c start taskkill /pid "+rm.getPid()+"  -t  -f ";
			//Process process = runtime.exec("cmd /c start "+cmd+"");//会打开一个新窗口后执行dir指令，原窗口会封闭。
			Process process = runtime.exec(result);
			Rtsp rtsp = new Rtsp();
			rtsp.setRtspId(rtspId);
			rtsp.setPid(-1);
			rtsp.setState(0);
			rtspService.updateById(rtsp);
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return JsonResult.ok() ;
	}
	
	/**
	 * 推流停止
	 * @param rtsp
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping(value="/rtmp/stopPush",method = {RequestMethod.POST})
	public JsonResult stopPush(Integer rtspId,HttpServletRequest request,HttpServletResponse response) {
		System.out.println("--------------------推流停止了");
		Rtsp rtsp = new Rtsp();
		rtsp.setRtspId(rtspId);
		rtsp.setState(0);
		if (rtspService.updateById(rtsp)) {
			return JsonResult.ok();
		} else {
			return JsonResult.error();
		}
		//return JsonResult.ok();
	}
	
	/**
	 * 添加摄像头视频
	 * @param rtsp
	 * @return
	 */
	@RequiresPermissions("rtsp:update")
	@ResponseBody
	@RequestMapping("/add")
	public JsonResult add(Rtsp rtsp) {
		rtsp.setState(0);
		if (rtspService.add(rtsp)) {
			return JsonResult.ok(200, "添加成功!");
		} else {
			return JsonResult.error(-1, "添加失败!");
		}
		
	}
	
	/**
	 * 修改摄像头视频
	 * @param rtsp
	 * @return
	 */
	@RequiresPermissions("rtsp:update")
	@ResponseBody
	@RequestMapping("/update")
	public JsonResult update(Rtsp rtsp) {
		if (rtspService.update(rtsp)) {
			return JsonResult.ok(200, "修改成功!");
		} else {
			return JsonResult.error(-1, "修改失败!");
		}
	}
	
	/**
	 * 删除摄像头视频
	 * @param rtspId
	 * @return
	 */
	@RequiresPermissions("rtsp:update")
	@ResponseBody
	@RequestMapping("/delete")
	public JsonResult delete(Integer rtspId) {
		if (rtspId == null) {
			return JsonResult.error(-1, "参数rtspId不能为空");
		}
		if (rtspService.removeById(rtspId)) {
			return JsonResult.ok(200, "删除成功");
		} else {
			return JsonResult.error(-1, "删除失败");
		}
	}
	
	/**
	 * 修改摄像头状态
	 * @param rtspId
	 * @param state
	 * @return
	 */
	@RequiresPermissions("rtsp:update")
	@ResponseBody
	@RequestMapping("/updateState")
	public JsonResult updateState (Integer rtspId ,Integer state) {
		if (rtspId == null) {
			return JsonResult.error(-1, "参数rtspId不能为空");
		}
		if (state == null || (state != 0 && state != 1)) {
            return JsonResult.error(-1,"状态值不正确");
        }
		Rtsp rtsp = new Rtsp();
		rtsp.setRtspId(rtspId);
		rtsp.setState(state);
		if (rtspService.updateById(rtsp)) {
			return JsonResult.ok();
		} else {
			return JsonResult.error();
		}
	}
	
	
}
