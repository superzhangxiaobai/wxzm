package com.wf.ew.light.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 能耗实体类
 * @author 
 *
 */
@TableName("light_energy")
public class Energy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "energy_id", type = IdType.AUTO)
	private Integer energyId ;
	
	 /**
     *路灯id
     */
	private Integer lampId;
	
	 /**
     *路灯名称
     */
	private String lampname;
	
	private String memo;
	
	 /**
     *路灯类型
     */
	private String remark;
	
	private String other;
	
	private Integer power;
	
	/**
     * 状态，0正常，1冻结
     */
    private Integer state;
    
    /**
     *创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

	public Integer getEnergyId() {
		return energyId;
	}

	public void setEnergyId(Integer energyId) {
		this.energyId = energyId;
	}

	public Integer getLampId() {
		return lampId;
	}

	public void setLampId(Integer lampId) {
		this.lampId = lampId;
	}

	public String getLampname() {
		return lampname;
	}

	public void setLampname(String lampname) {
		this.lampname = lampname;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}
	
	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}

	@Override
	public String toString() {
		 return "Energy{" +
	                ", energyId=" + energyId +
	                ", lampId=" + lampId +
	                ", lampname=" + lampname +
	                ", memo=" + memo +
	                ", remark=" + remark +
	                ", other=" + other +
	                ", state=" + state +
	                ", createTime=" + createTime +
	                ", updateTime=" + updateTime +
	                "}";
	}

}
