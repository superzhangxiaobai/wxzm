package com.wf.ew.light.utils;

import java.io.Serializable;
import java.util.Map;

/**
 * Http请求返回值对象
 * @author Administrator
 *
 */
public class ResponseResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer code;
	
	private String msg;
	
	private Integer obj;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getObj() {
		return obj;
	}

	public void setObj(Integer obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		 return "ResponseResult{" +
	                "code=" + code +
	                ", msg=" + msg +
	                ", obj=" + obj +
	                "}";
	}
	
}
