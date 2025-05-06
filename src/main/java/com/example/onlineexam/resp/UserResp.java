package com.example.onlineexam.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class UserResp {

    /**
     * 用户ID
     */
    private Integer uid;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像url
     */
    private String avatar;

    /**
     * 主页背景图url
     */
    private String background;

    /**
     * 性别 0女 1男 2未知
     */
    private Integer gender;

    /**
     * 个性签名
     */
    private String description;

    /**
     * 经验值
     */
    private Integer exp;

    /**
     * 硬币数
     */
    private String coin;

    /**
     * 会员类型 0普通用户 1月度大会员 2季度大会员 3年度大会员
     */
    private Integer vip;

    /**
     * 状态 0正常 1封禁 2注销
     */
    private Integer state;

    /**
     * 角色类型 0普通用户 1管理员 2超级管理员
     */
    private Integer role;

    /**
     * 官方认证 0普通用户 1个人认证 2机构认证
     */
    private Integer auth;

    /**
     * 认证说明
     */
    private String authMsg;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    /**
     * 注销时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date deleteDate;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getAuth() {
        return auth;
    }

    public void setAuth(Integer auth) {
        this.auth = auth;
    }

    public String getAuthMsg() {
        return authMsg;
    }

    public void setAuthMsg(String authMsg) {
        this.authMsg = authMsg;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", uid=").append(uid);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", nickname=").append(nickname);
        sb.append(", avatar=").append(avatar);
        sb.append(", background=").append(background);
        sb.append(", gender=").append(gender);
        sb.append(", description=").append(description);
        sb.append(", exp=").append(exp);
        sb.append(", coin=").append(coin);
        sb.append(", vip=").append(vip);
        sb.append(", state=").append(state);
        sb.append(", role=").append(role);
        sb.append(", auth=").append(auth);
        sb.append(", authMsg=").append(authMsg);
        sb.append(", createDate=").append(createDate);
        sb.append(", deleteDate=").append(deleteDate);
        sb.append("]");
        return sb.toString();
    }
}
