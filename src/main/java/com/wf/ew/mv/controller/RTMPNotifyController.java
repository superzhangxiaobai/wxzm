package com.wf.ew.mv.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/v1/rtmp/")
public class RTMPNotifyController {

	
	
	
	@PostMapping("/auth")
	public @ResponseBody String auth(String passWord,HttpServletRequest request,HttpServletResponse response){
		System.out.println("aaaaa");
		return "{\"code\":\"200\",\"detail\":\"SUCCESS\"}";
	}
	
	
    @GetMapping(value="/on_connect")
    public String onConnect(HttpServletRequest request){
        debug(request, "on_connect");
        //return "on_connect";
        return "{\"code\":\"200\",\"detail\":\"SUCCESS\"}"; 
    }
    @GetMapping(value="/on_play")
    public String onPlay(HttpServletRequest request){
        debug(request, "on_play");
        //return "on_play";
        return "{\"code\":\"200\",\"detail\":\"SUCCESS\"}"; 
    }
    @PostMapping(value="/on_publish")
    public String onPublish(HttpServletRequest request){
        //debug(request, "on_publish");
        System.out.println("----------------------------");
        //return "on_publish";
        return "{\"code\":\"200\",\"detail\":\"SUCCESS\"}"; 
    }
    @GetMapping(value="/on_done")
    public String onDone(HttpServletRequest request){
        debug(request, "on_done");
        //return "on_done";
        return "{\"code\":\"200\",\"detail\":\"SUCCESS\"}"; 
    }
    @GetMapping(value="/on_play_done")
    public String onPlayDone(HttpServletRequest request){
        debug(request, "on_play_done");
        //return "on_play_done";
        return "{\"code\":\"200\",\"detail\":\"SUCCESS\"}"; 
    }
    @GetMapping(value="/on_publish_done")
    public String onPublishDone(HttpServletRequest request){
        debug(request, "on_publish_done");
        System.out.println("-------------------------");
        //return "on_publish_done";
        return "{\"code\":\"200\",\"detail\":\"SUCCESS\"}"; 
    }
    @GetMapping(value="/on_record_done")
    public String onRecordDone(HttpServletRequest request){
        debug(request, "on_record_done");
        //return "on_record_done";
        return "{\"code\":\"200\",\"detail\":\"SUCCESS\"}"; 
    }
    @GetMapping(value="/on_update")
    public String onUpdate(HttpServletRequest request){
        debug(request, "on_update");
        //return "on_update";
        return "{\"code\":\"200\",\"detail\":\"SUCCESS\"}"; 
    }
    
    private String debug(HttpServletRequest request, String action){
        String str = action + ": " + request.getRequestURI() + " " + request.getQueryString();
        System.out.println(str);
        return str;
    }
}