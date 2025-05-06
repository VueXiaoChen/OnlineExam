package com.example.onlineexam.domain;

import java.util.Date;

public class UserVideo {
    private Integer id;

    private Integer uid;

    private Integer vid;

    private Integer play;

    private Integer love;

    private Integer unlove;

    private Integer coin;

    private Integer collect;

    private Date playTime;

    private Date loveTime;

    private Date coinTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getVid() {
        return vid;
    }

    public void setVid(Integer vid) {
        this.vid = vid;
    }

    public Integer getPlay() {
        return play;
    }

    public void setPlay(Integer play) {
        this.play = play;
    }

    public Integer getLove() {
        return love;
    }

    public void setLove(Integer love) {
        this.love = love;
    }

    public Integer getUnlove() {
        return unlove;
    }

    public void setUnlove(Integer unlove) {
        this.unlove = unlove;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public Integer getCollect() {
        return collect;
    }

    public void setCollect(Integer collect) {
        this.collect = collect;
    }

    public Date getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }

    public Date getLoveTime() {
        return loveTime;
    }

    public void setLoveTime(Date loveTime) {
        this.loveTime = loveTime;
    }

    public Date getCoinTime() {
        return coinTime;
    }

    public void setCoinTime(Date coinTime) {
        this.coinTime = coinTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uid=").append(uid);
        sb.append(", vid=").append(vid);
        sb.append(", play=").append(play);
        sb.append(", love=").append(love);
        sb.append(", unlove=").append(unlove);
        sb.append(", coin=").append(coin);
        sb.append(", collect=").append(collect);
        sb.append(", playTime=").append(playTime);
        sb.append(", loveTime=").append(loveTime);
        sb.append(", coinTime=").append(coinTime);
        sb.append("]");
        return sb.toString();
    }
}