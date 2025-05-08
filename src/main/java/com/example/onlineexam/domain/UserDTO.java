package com.example.onlineexam.domain;




public class UserDTO {
    private Integer uid;
    private String nickname;
    private String avatar_url;
    private String bg_url;
    private Integer gender; // 性别，0女性 1男性 2无性别，默认2
    private String description;
    private Integer exp;    // 经验值 50/200/1500/4500/10800/28800 分别是0~6级的区间
    private Double coin;    // 硬币数  保留一位小数
    private Integer vip;    // 0 普通用户，1 月度大会员，2 季度大会员，3 年度大会员
    private Integer state;  // 0 正常，1 封禁中
    private Integer auth;   // 0 普通用户，1 个人认证，2 机构认证
    private String authMsg; // 认证信息，如 teriteri官方账号
    private Integer videoCount; // 视频投稿数
    private Integer followsCount;   // 关注数
    private Integer fansCount;  // 粉丝数
    private Integer loveCount;  // 获赞数
    private Integer playCount;  // 播放数

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getBg_url() {
        return bg_url;
    }

    public void setBg_url(String bg_url) {
        this.bg_url = bg_url;
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

    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public Double getCoin() {
        return coin;
    }

    public void setCoin(Double coin) {
        this.coin = coin;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public Integer getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(Integer videoCount) {
        this.videoCount = videoCount;
    }

    public Integer getFollowsCount() {
        return followsCount;
    }

    public void setFollowsCount(Integer followsCount) {
        this.followsCount = followsCount;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public Integer getLoveCount() {
        return loveCount;
    }

    public void setLoveCount(Integer loveCount) {
        this.loveCount = loveCount;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "uid=" + uid +
                ", nickname='" + nickname + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", bg_url='" + bg_url + '\'' +
                ", gender=" + gender +
                ", description='" + description + '\'' +
                ", exp=" + exp +
                ", coin=" + coin +
                ", vip=" + vip +
                ", state=" + state +
                ", auth=" + auth +
                ", authMsg='" + authMsg + '\'' +
                ", videoCount=" + videoCount +
                ", followsCount=" + followsCount +
                ", fansCount=" + fansCount +
                ", loveCount=" + loveCount +
                ", playCount=" + playCount +
                '}';
    }
}
