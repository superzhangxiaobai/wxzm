package com.wf.ew.light.model;

import java.io.Serializable;
import java.time.LocalDateTime;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 路灯任务实体类
 * @author 
 *
 */
@TableName("light_task")
public class LampTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "task_id" ,type = IdType.AUTO)
	private Integer taskId ;
	
	private String opencron ;//开灯表达式
	
	private String closecron ;//关灯表达式
	
	private String memo ;//备注
	
	private String remark ;//备用
	
	private String other ;//其他
	
	private Integer leixing ;//路灯类型
	
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

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getOpencron() {
		return opencron;
	}

	public void setOpencron(String opencron) {
		this.opencron = opencron;
	}

	public String getClosecron() {
		return closecron;
	}

	public void setClosecron(String closecron) {
		this.closecron = closecron;
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

	public Integer getLeixing() {
		return leixing;
	}

	public void setLeixing(Integer leixing) {
		this.leixing = leixing;
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
		 return "LampTask{" +
	                ", taskId=" + taskId +
	                ", opencron=" + opencron +
	                ", closecron=" + closecron +
	                ", memo=" + memo +
	                ", remark=" + remark +
	                ", other=" + other +
	                ", leixing=" + leixing +
	                ", state=" + state +
	                ", createTime=" + createTime +
	                ", updateTime=" + updateTime +
	                "}";
	}

}
