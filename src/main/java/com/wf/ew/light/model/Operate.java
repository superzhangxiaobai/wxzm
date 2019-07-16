package com.wf.ew.light.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 操作实体类
 * @author 
 *
 */
@TableName("light_operate")
public class Operate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "operate_id", type = IdType.AUTO)
	private Integer operateId ;
	
	/**
     * 操作人员昵称
     */
	private String nickname;
	/**
     * 操作内容
     */
	private String operatecontent;
	/**
     * 备注
     */
	private String memo;
	/**
     * 备用
     */
	private String remark;
	/**
     * 其他
     */
	private String other;
	
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

	public Integer getOperateId() {
		return operateId;
	}

	public void setOperateId(Integer operateId) {
		this.operateId = operateId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getOperatecontent() {
		return operatecontent;
	}

	public void setOperatecontent(String operatecontent) {
		this.operatecontent = operatecontent;
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
	
	@Override
	public String toString() {
		 return "Operate{" +
	                ", operateId=" + operateId +
	                ", nickname=" + nickname +
	                ", operatecontent=" + operatecontent +
	                ", memo=" + memo +
	                ", remark=" + remark +
	                ", other=" + other +
	                ", state=" + state +
	                ", createTime=" + createTime +
	                ", updateTime=" + updateTime +
	                "}";
	}

}
