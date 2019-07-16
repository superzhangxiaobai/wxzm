package com.wf.ew.mv.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wf.ew.common.BaseController;

@Controller
@RequestMapping("/mv/rtmp")
public class RtmpController extends BaseController {

	@RequiresPermissions("rtmp:view")
	@RequestMapping
	public String rtmp (Model model) {
		return "mv/rtmp.html" ;
	}
	
	/*@RequestMapping("ckplayer.swf")
	public String ckplayer (Model model) {
		return "mv/ckplayer.swf" ;
	}*/
	
	@GetMapping(value = "/{preview}",produces = MediaType.ALL_VALUE)
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
	
}
