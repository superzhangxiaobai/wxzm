package com.wf.ew.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author wangfan
 * @since 2019-02-11
 */

@TableName("sys_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @ApiModelProperty(value="用户标识",dataType="Integer",name="userId",example="8",hidden=true)
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    @ApiModelProperty(value="用户昵称",dataType="String",name="nickName",example="威望")
    private String nickName;

    /**
     * 头像
     */
    @ApiModelProperty(value="头像",dataType="String",name="avatar",example="875e8b0ba21c4e228983c78364c11104.jpg")
    private String avatar;

    /**
     * 性别
     */
    private String sex;

    /**
     * 手机号
     */
    @ApiModelProperty(value="手机号",dataType="String",name="phone",example="13888888887")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value="邮箱",dataType="String",name="email",example="xxx@163.com")
    private String email;

    /**
     * 邮箱是否验证,0未验证,1已验证
     */
    private Integer emailVerified;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value="姓名",dataType="String",name="trueName",example="张三")
    private String trueName;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 出生日期
     */
    private LocalDate birthday;

    /**
     * 部门id
     */
    private Integer departmentId;

    /**
     * 状态，0正常，1冻结
     */
    private Integer state;

    /**
     * 注册时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private List<Role> roles;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Integer emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                ", userId=" + userId +
                ", username=" + username +
                ", password=" + password +
                ", nickName=" + nickName +
                ", avatar=" + avatar +
                ", sex=" + sex +
                ", phone=" + phone +
                ", email=" + email +
                ", emailVerified=" + emailVerified +
                ", trueName=" + trueName +
                ", idCard=" + idCard +
                ", birthday=" + birthday +
                ", departmentId=" + departmentId +
                ", state=" + state +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }

}
