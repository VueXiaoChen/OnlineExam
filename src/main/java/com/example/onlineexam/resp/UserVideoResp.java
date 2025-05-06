package com.example.onlineexam.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class UserVideoResp {

    /**
     * 唯一标识
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Integer id;

    /**
     * 观看视频的用户UID
     */
    private Integer uid;

    /**
     * 视频ID
     */
    private Integer vid;

    /**
     * 播放次数
     */
    private Integer play;

    /**
     * 点赞 0没赞 1已点赞
     */
    private Integer love;

    /**
     * 不喜欢 0没点 1已不喜欢
     */
    private Integer unlove;

    /**
     * 投币数 0-2 默认0
     */
    private Integer coin;

    /**
     * 收藏 0没收藏 1已收藏
     */
    private Integer collect;

    /**
     * 最近播放时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date playTime;

    /**
     * 点赞时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date loveTime;

    /**
     * 投币时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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
