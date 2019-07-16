package com.wf.ew.light.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description="返回类",value="照明实体")
public class LampApi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 路灯Id */
	@ApiModelProperty(value = "路灯Id")
	private Integer lampId;
	
	/** 路灯名称 */
	@ApiModelProperty(value = "灯塔名称",example = "lamp-name",name = "myname")
	private String lampName;
	/** 经度 */
	@ApiModelProperty(value = "经度",example = "lamp-name",name = "myname")
	private Double longitude;
	/** 纬度 */
	@ApiModelProperty(value = "纬度")
	private Double latitude;
	/** 路灯状态 */
	@ApiModelProperty(value = "灯塔状态，开灯：1，关灯：0，报警：2")
	private Integer state;
	/** 路灯状态 */
	@ApiModelProperty(value = "灯塔状态中文说明")
	private String stateMessage;
	/** 路灯类别 */
	@ApiModelProperty(value = "灯塔类别，【灯塔25，灯塔13，路灯】")
	private String category;
	/** 灯杆状态 */
	@ApiModelProperty(value = "灯杆状态，异常：-1，正常：0")
	private Integer poleState;
	/** 灯杆状态 */
	@ApiModelProperty(value = "灯杆状态中文说明")
	private String poleMessage;
	
	public Integer getLampId() {
		return lampId;
	}
	public void setLampId(Integer lampId) {
		this.lampId = lampId;
	}
	
	public String getLampName() {
		return lampName;
	}
	public void setLampName(String lampName) {
		this.lampName = lampName;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getStateMessage() {
		return stateMessage;
	}
	public void setStateMessage(String stateMessage) {
		this.stateMessage = stateMessage;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public Integer getPoleState() {
		return poleState;
	}
	public void setPoleState(Integer poleState) {
		this.poleState = poleState;
	}
	public String getPoleMessage() {
		return poleMessage;
	}
	public void setPoleMessage(String poleMessage) {
		this.poleMessage = poleMessage;
	}
	@Override
	public String toString() {
		 return "LampApi{" +
	                ", lampId=" + lampId +
	                ", lampname=" + lampName +
	                ", longitude=" + longitude +
	                ", latitude=" + latitude +
	                ", state=" + state +
	                ", stateMessage=" + stateMessage +
	                ", category=" + category +
	                ", poleState=" + poleState +
	                ", poleMessage=" + poleMessage +
	                "}";
	}

}
