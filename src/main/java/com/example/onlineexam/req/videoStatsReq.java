package com.example.onlineexam.req;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class videoStatsReq extends PageReq{

    /**
     * 视频ID
     */

    private Integer vid;

    /**
     * 播放量
     */

    private Integer play;

    /**
     * 弹幕数
     */

    private Integer danmu;

    /**
     * 点赞数
     */

    private Integer good;

    /**
     * 点踩数
     */

    private Integer bad;

    /**
     * 投币数
     */

    private Integer coin;

    /**
     * 收藏数
     */

    private Integer collect;

    /**
     * 分享数
     */

    private Integer share;

    /**
     * 评论数量统计
     */
    private Integer comment;

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

    public Integer getDanmu() {
        return danmu;
    }

    public void setDanmu(Integer danmu) {
        this.danmu = danmu;
    }

    public Integer getGood() {
        return good;
    }

    public void setGood(Integer good) {
        this.good = good;
    }

    public Integer getBad() {
        return bad;
    }

    public void setBad(Integer bad) {
        this.bad = bad;
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

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", vid=").append(vid);
        sb.append(", play=").append(play);
        sb.append(", danmu=").append(danmu);
        sb.append(", good=").append(good);
        sb.append(", bad=").append(bad);
        sb.append(", coin=").append(coin);
        sb.append(", collect=").append(collect);
        sb.append(", share=").append(share);
        sb.append(", comment=").append(comment);
        sb.append("]");
        return sb.toString();
    }
}
