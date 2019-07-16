package com.wf.ew.mv.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 
 * * <p>
 * 摄像头视频表
 * </p>
 * 
 * @author null
 * @since 2019-03-21
 *
 */
@TableName("mv_rtsp")
public class Rtsp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 /**
     * 摄像头视频id
     */
    @TableId(value = "rtsp_id", type = IdType.AUTO)
    private Integer rtspId;

    /**
     * 摄像头视频名称
     */
    private String videoname;

    /**
     * 摄像头视频流地址
     */
    private String videourl;

    /**
     * 摄像头安装地址
     */
    private String videoaddress;
    
    private String rtmpurl ;
    private String cmdpath;
    private Double lat ;
    private Double lng ;
    private Integer pid;
    
    public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getRtmpurl() {
		return rtmpurl;
	}

	public void setRtmpurl(String rtmpurl) {
		this.rtmpurl = rtmpurl;
	}

	public String getCmdpath() {
		return cmdpath;
	}

	public void setCmdpath(String cmdpath) {
		this.cmdpath = cmdpath;
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

	/**
     * 推流服务器地址
     */
    @TableField(value = "rtmp_id")
    private Integer rtmpId;
    
    /**
     * 状态，0正常，1不可用
     */
    private Integer state;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

	public Integer getRtspId() {
		return rtspId;
	}

	public void setRtspId(Integer rtspId) {
		this.rtspId = rtspId;
	}

	public String getVideoname() {
		return videoname;
	}

	public void setVideoname(String videoname) {
		this.videoname = videoname;
	}

	public String getVideourl() {
		return videourl;
	}

	public void setVideourl(String videourl) {
		this.videourl = videourl;
	}

	public String getVideoaddress() {
		return videoaddress;
	}

	public void setVideoaddress(String videoaddress) {
		this.videoaddress = videoaddress;
	}

	public Integer getRtmpId() {
		return rtmpId;
	}

	public void setRtmpId(Integer rtmpId) {
		this.rtmpId = rtmpId;
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
        return "Rtsp{" +
                ", rtspId=" + rtspId +
                ", videoname=" + videoname +
                ", videourl=" + videourl +
                ", videoaddress=" + videoaddress +
                ", rtmpId=" + rtmpId +
                ", state=" + state +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }

}
