package com.wf.ew.light.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.wf.ew.common.BaseController;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class ImageController extends BaseController {

	@GetMapping(value = "images/loading/{preview}",produces = MediaType.ALL_VALUE)
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
