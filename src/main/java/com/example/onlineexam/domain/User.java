package com.example.onlineexam.domain;

import java.util.Date;

public class User {
    private Integer uid;

    private String username;

    private String password;

    private String nickname;

    private String avatar;

    private String background;

    private Byte gender;

    private String description;

    private Integer exp;

    private Double coin;

    private Byte vip;

    private Byte state;

    private Byte role;

    private Byte auth;

    private String authMsg;

    private Date createDate;

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

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
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

    public Double getCoin() {
        return coin;
    }

    public void setCoin(Double coin) {
        this.coin = coin;
    }

    public Byte getVip() {
        return vip;
    }

    public void setVip(Byte vip) {
        this.vip = vip;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Byte getRole() {
        return role;
    }

    public void setRole(Byte role) {
        this.role = role;
    }

    public Byte getAuth() {
        return auth;
    }

    public void setAuth(Byte auth) {
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