package com.wf.ew.light.utils;

import java.io.Serializable;

/**
 * Http请求参数封装
 * @author Administrator
 *
 */
public class RequestParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ip;
	
	private Integer channel;
	
	private Integer state;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
