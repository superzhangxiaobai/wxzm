package com.wf.ew.light.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wf.ew.common.BaseController;
import com.wf.ew.common.JsonResult;
import com.wf.ew.common.utils.StringUtil;
import com.wf.ew.system.model.User;
import com.wf.ew.system.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 个人信息接口类
 * @author 
 *
 */

@Api(description="个人信息接口类")
@Controller
@RequestMapping("/light/user")
public class PersonController extends BaseController {

	@Autowired
	private UserService userService;
	
	/**
     * 个人信息修改
     **/
	@ApiOperation(value="个人信息修改",notes="个人信息修改", httpMethod = "POST")
	@ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User",paramType="body")
	@ResponseBody
    @RequestMapping("/update")
    public JsonResult update(User user) {
		/*if (StringUtil.isBlank(user.getAvatar())) {
			user.setAvatar(null);
		}
		if (StringUtil.isBlank(user.getTrueName())) {
			user.setTrueName(null);
		}
		if (StringUtil.isBlank(user.getPhone())) {
			user.setPhone(null);
		}
		if (StringUtil.isBlank(user.getEmail())) {
			user.setEmail(null);
		}*/
		user.setUpdateTime(null);
        if (userService.updateById(user)) {
            return JsonResult.ok("修改成功");
        } else {
            return JsonResult.error("修改失败");
        }
    }
	
	/**
     * 上传个人图片
     **/
	@ApiOperation(value="上传个人图片",notes="上传个人图片", httpMethod = "POST")
    @ResponseBody
    @RequestMapping("/upload")
    public JsonResult upload(@ApiParam(value = "图片文件", required = false) @RequestParam(value = "file",required = false) MultipartFile file) {
		if (file == null) {
			return JsonResult.error("请选择图片文件");
		}
    	if (!file.isEmpty()) {
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
			File fileSourcePath = new File("img");
			if (!fileSourcePath.exists()) {
				fileSourcePath.mkdirs();
			}
			try {
				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(new File(fileSourcePath, fileName.toString())));
				out.write(file.getBytes());
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
     * 显示个人图片
     **/
    @ApiOperation(value="显示个人图片",notes="显示个人图片", httpMethod = "GET")
    @GetMapping(value = "/img/{preview}",produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> preview(@ApiParam(value = "图片ID", required = true) @PathVariable("preview") String id){
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
	
}
