package com.wf.ew.light.utils;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.wf.ew.light.model.Lamp;

public class TestHttp {

	public static void main(String[] args) {
		//HttpClientUtil.sendGet("http://192.168.101.172:8000/");
		//https://github.com/Arronlong/httpclientutil
		//https://www.monodevelop.com/
		
		/*ResponseResult result = JSONObject.parseObject("{'code':200,'msg':'返回成功',obj:{}}", ResponseResult.class);
		System.out.println(result.toString());*/
		Lamp lamp = new Lamp();
		lamp.setRemark("192.168.101.209");
		lamp.setChannel("1");
		//HttpClientUtil.sendPost("http://192.168.101.172:8000/controll", lamp,true);
		//ResponseResult sendPost = HttpClientUtil.sendPost("http://192.168.101.55:8000/controll", lamp,true);
		ResponseResult sendPostOpen = HttpClientUtil.sendPost("http://192.168.101.55:8000/open", lamp,true);
		ResponseResult sendPost = HttpClientUtil.sendPost("http://192.168.101.55:8000/getledchannelvalue", lamp,false);
		System.out.println("查询单个通道状态======>"+sendPost.toString());
		ResponseResult sendPostOpenClose = HttpClientUtil.sendPost("http://192.168.101.55:8000/close", lamp,true);
		//ResponseResult sendPost = HttpClientUtil.sendPost("http://192.168.101.55:8000/ledopenorclose", lamp,true);
		//System.out.println("result--------------->"+sendPost.getCode()+",,,"+sendPost.getMsg());
		/*List<String> ipList = new ArrayList<>();
		ipList.add("com1");
		ipList.add("com2");
		ipList.add("com3");
		System.out.println("长度---------------"+ipList.size());
		if (ipList.contains("com3")) {
			System.out.println("包含com3");
		}
		ipList.clear();
		System.out.println("清空后长度--------------"+ipList.size());
		if (ipList.contains("com3")) {
			System.out.println("包含com3");
		}else {
			System.out.println("清空后没有com3了");
		}*/
	}
	
	
}
