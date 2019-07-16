package com.wf.ew.light.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 路灯实体类
 * @author 
 *
 */
@TableName("light_lamp")
public class Lamp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "lamp_id", type = IdType.AUTO)
	private Integer lampId ;
	
	private String lampname ;//账号
	
	private String avatar ;//开灯图像
	
	private String nickName ;//昵称
	
	private String memo ;//备注,命令地址
	
	private String unavatar ;//关灯图像
	
	private String remark ;//备用,串口
	
	private Integer lampBefore ;//前路灯
	
	private Integer lampAfter ;//后路灯
	
	private Integer lampLeft ;//左路灯
	
	private Integer lampRight ;//右路灯
	
	private Integer lampWarn ;//故障灯
	
	private Double lat ;//经度
	
	private Double lng ;//纬度
	
	private String code ;//命令
	
	private String groupIng ;//分组,mysql8关键字问题
	
	private String channel ;//通道
	
	private String category ;//类别
	
	
	private String secondlevel ;//二级类别
	
	private Integer lampSecond ;//二级路灯
	
	private String secondchannel ;//二级通道
	
	private Integer power ;//路灯功率
	
	private Integer secondpower ;//二级功率
	
	private Integer baudRate;//波特率
	private Integer checkoutBit;//校验位
	private Integer dataBit;//数据位
	private Integer stopBit;//停止位
	private String other;//其他
	
	/*@TableField(value = "memo1", strategy = FieldStrategy.IGNORED)*/
	private String memo1;//备注1
	private String memo2;//备注2
	private String memo3;//备注3
	private Integer remark1;//备用1,命令地址十进制
	private Integer remark2;//备用2,灯杆状态，0正常，-1异常
	private Double longitude;//经度
	private Double latitude;//纬度
	
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

	public Integer getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(Integer baudRate) {
		this.baudRate = baudRate;
	}

	public Integer getCheckoutBit() {
		return checkoutBit;
	}

	public void setCheckoutBit(Integer checkoutBit) {
		this.checkoutBit = checkoutBit;
	}

	public Integer getDataBit() {
		return dataBit;
	}

	public void setDataBit(Integer dataBit) {
		this.dataBit = dataBit;
	}

	public Integer getStopBit() {
		return stopBit;
	}

	public void setStopBit(Integer stopBit) {
		this.stopBit = stopBit;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getMemo1() {
		return memo1;
	}

	public void setMemo1(String memo1) {
		this.memo1 = memo1;
	}

	public String getMemo2() {
		return memo2;
	}

	public void setMemo2(String memo2) {
		this.memo2 = memo2;
	}

	public String getMemo3() {
		return memo3;
	}

	public void setMemo3(String memo3) {
		this.memo3 = memo3;
	}

	public Integer getRemark1() {
		return remark1;
	}

	public void setRemark1(Integer remark1) {
		this.remark1 = remark1;
	}

	public Integer getRemark2() {
		return remark2;
	}

	public void setRemark2(Integer remark2) {
		this.remark2 = remark2;
	}

	public Integer getSecondpower() {
		return secondpower;
	}

	public void setSecondpower(Integer secondpower) {
		this.secondpower = secondpower;
	}

	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}

	public Integer getLampSecond() {
		return lampSecond;
	}

	public void setLampSecond(Integer lampSecond) {
		this.lampSecond = lampSecond;
	}

	public String getSecondchannel() {
		return secondchannel;
	}

	public void setSecondchannel(String secondchannel) {
		this.secondchannel = secondchannel;
	}

	public String getSecondlevel() {
		return secondlevel;
	}

	public void setSecondlevel(String secondlevel) {
		this.secondlevel = secondlevel;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}


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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getUnavatar() {
		return unavatar;
	}

	public void setUnavatar(String unavatar) {
		this.unavatar = unavatar;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getLampBefore() {
		return lampBefore;
	}

	public void setLampBefore(Integer lampBefore) {
		this.lampBefore = lampBefore;
	}

	public Integer getLampAfter() {
		return lampAfter;
	}

	public void setLampAfter(Integer lampAfter) {
		this.lampAfter = lampAfter;
	}

	public Integer getLampLeft() {
		return lampLeft;
	}

	public void setLampLeft(Integer lampLeft) {
		this.lampLeft = lampLeft;
	}

	public Integer getLampRight() {
		return lampRight;
	}

	public void setLampRight(Integer lampRight) {
		this.lampRight = lampRight;
	}

	public Integer getLampWarn() {
		return lampWarn;
	}

	public void setLampWarn(Integer lampWarn) {
		this.lampWarn = lampWarn;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGrouping() {
		return groupIng;
	}

	public void setGrouping(String grouping) {
		this.groupIng = grouping;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
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
		 return "Lamp{" +
	                ", lampId=" + lampId +
	                ", lampname=" + lampname +
	                ", avatar=" + avatar +
	                ", nickName=" + nickName +
	                ", memo=" + memo +
	                ", unavatar=" + unavatar +
	                ", remark=" + remark +
	                ", lampBefore=" + lampBefore +
	                ", lampAfter=" + lampAfter +
	                ", lampLeft=" + lampLeft +
	                ", lampRight=" + lampRight +
	                ", lampWarn=" + lampWarn +
	                ", lat=" + lat +
	                ", lng=" + lng +
	                ", code=" + code +
	                ", state=" + state +
	                ", createTime=" + createTime +
	                ", updateTime=" + updateTime +
	                "}";
	}

}
