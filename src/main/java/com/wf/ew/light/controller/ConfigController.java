package com.wf.ew.light.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
//import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wf.ew.common.BaseController;
import com.wf.ew.common.JsonResult;
import com.wf.ew.common.PageResult;
import com.wf.ew.common.utils.JSONUtil;
import com.wf.ew.light.model.Lamp;
import com.wf.ew.light.service.LampService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 路灯配置接口类
 * @author 
 *
 */

@Api(description="路灯配置接口类",position=2)
@Controller
@RequestMapping("/light/config")
public class ConfigController extends BaseController {

	@Autowired
	private LampService lampService ;
	
	/*@Autowired
    private MongoTemplate mongoTemplate;*/
	
	@RequiresPermissions("config:view")
	@RequestMapping
	public String config (Model model) {
		return "light/config.html" ;
	}
	
	 /**
     * 查询
     */
    @RequiresPermissions("config:view")
    @ResponseBody
    @RequestMapping("/list")
	public PageResult<Lamp> list(Integer page, Integer limit, String searchKey, String searchValue) {
		return lampService.listLamp(page, limit, searchKey, searchValue);
	}
    
    
    /**
     * 添加
     **/
    @ApiOperation(value="添加路灯配置信息",notes="添加路灯配置信息", httpMethod = "POST")
    @RequiresPermissions("config:update")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(Lamp lamp) {
    	lamp.setState(0);
    	//定义一个十进制值
		int valueTen = lamp.getRemark1();
		//将其转换为十六进制并输出
		String strHex = Integer.toHexString(valueTen);
		strHex = (strHex.length()==1?"0"+strHex.toUpperCase():strHex.toUpperCase());
		lamp.setMemo(strHex);
        if (lampService.add(lamp)) {
            return JsonResult.ok("添加成功");
        } else {
            return JsonResult.error("添加失败");
        }
    }

    /**
     * 修改
     **/
    @RequiresPermissions("config:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Lamp lamp) {
    	//定义一个十进制值
		int valueTen = lamp.getRemark1();
		//将其转换为十六进制并输出
		String strHex = Integer.toHexString(valueTen);
		strHex = (strHex.length()==1?"0"+strHex.toUpperCase():strHex.toUpperCase());
		lamp.setMemo(strHex);
        if (lampService.update(lamp)) {
        	System.out.println("-------------------------什么时候走");
        	//mongoTemplate.save(lamp,"lampconfig");
        	
            return JsonResult.ok("修改成功");
        } else {
            return JsonResult.error("修改失败");
        }
    }
    
    /**
     * 修改状态
     **/
    @RequiresPermissions("config:update")
    @ResponseBody
    @RequestMapping("/updateState")
    public JsonResult updateState(Integer lampId, Integer state) {
        if (lampId == null) {
            return JsonResult.error("参数lampId不能为空");
        }
        if (state == null || (state != 0 && state != 1)) {
            return JsonResult.error("状态值不正确");
        }
       
        Lamp lamp = new Lamp();
        lamp.setLampId(lampId);
        lamp.setState(state);
        if (lampService.updateById(lamp)) {
            return JsonResult.ok();
        } else {
            return JsonResult.error();
        }
    }

    /**
     * 删除
     **/
    @RequiresPermissions("config:update")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(Integer lampId) {
        if (lampId == null) {
            return JsonResult.error("参数lampId不能为空");
        }
        if (lampService.removeById(lampId)) {
            return JsonResult.ok("删除成功");
        } else {
            return JsonResult.error("删除失败");
        }
    }
    
    /**
     * 上传图片
     **/
    @RequiresPermissions("config:update")
    @ResponseBody
    @RequestMapping("/upload")
    public JsonResult upload(@RequestParam(value = "file",required = false) MultipartFile file) {
    	if (!file.isEmpty()) {
			//String fileName = file.getOriginalFilename() ;
			
			//String type = file.getContentType() ;
			//System.out.println("type======>"+type);
			StringBuffer fileName = new StringBuffer();
	        fileName.append(UUID.randomUUID().toString().replaceAll("-", ""));
	        String type = file.getContentType();
	        if ("image/png".equals(type)) {
	            fileName.append(".png");
	        } else if ("image/jpeg".equals(type)) {
	            fileName.append(".jpg");
	        } else if ("image/gif".equals(type)) {
	            fileName.append(".gif");
	        } else {
	        	return JsonResult.error("图片格式不正确");
	        }
	        if (file.getSize() > 1024000L) {
	        	return JsonResult.error("图片超过1Mb");
	        }
			//SimpleDateFormat sf = new SimpleDateFormat("yyyymmddHHmmss");
			//fileName = sf.format(System.currentTimeMillis())+"."+ext ;
			File fileSourcePath = new File("img");
			if (!fileSourcePath.exists()) {
				fileSourcePath.mkdirs();
			}
			try {
				//fileName = "123" ;
				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(new File(fileSourcePath, fileName.toString())));
				out.write(file.getBytes());
				//file.transferTo(new File(imagePath, fileName.toString()));
				out.flush();
				out.close();
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			return JsonResult.ok("上传成功").put("img", fileName);
		}else {
			return JsonResult.error("上传失败");
		}
        //return null ;
    }
    
    /**
     * 获取图片
     * @param id
     * @return
     */
    @GetMapping(value = "/img/{preview}",produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> preview(@PathVariable("preview") String id){
    	try {
			InputStream in = new FileInputStream(new File("img/"+id));
			InputStreamResource inr = new InputStreamResource(in);
			HttpHeaders headers = new HttpHeaders();
			System.out.println("文件名："+id);
			return new ResponseEntity<Resource>(inr, headers,HttpStatus.OK);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    /**
     * 查询单个路灯
     **/
    @ResponseBody
    @GetMapping("/get")
	public JsonResult get (Integer lampId) {
    	//http://www.layui.com/doc/modules/form.html#oncheckbox
    	if (lampId == null) {
            return JsonResult.error("参数lampId不能为空");
        }
    	Lamp lamp = lampService.getById(lampId);
    	if (lamp != null && lamp.getLampname() != null) {
    		 return JsonResult.ok("存在").put("lamp", lamp);
		} else {
			return JsonResult.error("不存在");
		}
		//return null ;
	}
    
    /**
     * 修改灯塔的路灯，路灯功率等
     */
    @RequiresPermissions("config:update")
    @ResponseBody
    @PostMapping("/updateSecondLevel")
    public JsonResult updateSecondLevel(Integer lampId, String secondlevel,String secondchannel,Integer power,Integer secondpower) {
    	Lamp lamp = new Lamp();
    	lamp.setLampId(lampId);
    	
    	lamp.setSecondlevel(secondlevel);
    	lamp.setSecondchannel(secondchannel);
    	lamp.setSecondpower(secondpower);
    	lamp.setPower(power);
        if (lampService.updateById(lamp)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }
    
    /**
     * 修改路灯的经纬度
     */
    @RequiresPermissions("config:update")
    @ResponseBody
    @PostMapping("/updateLng")
    public JsonResult updateLngLat(Integer lampId,String memo1,String memo3, Double longitude,Double latitude) {
    	Lamp lamp = new Lamp();
    	lamp.setLampId(lampId);
    	lamp.setLongitude(longitude);
    	lamp.setLatitude(latitude);
    	lamp.setMemo3(memo3);
    	if (!memo1.equals("")) {
			lamp.setMemo1(memo1);
		}
        if (lampService.updateById(lamp)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }
	
	
}
