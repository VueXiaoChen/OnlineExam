package com.example.onlineexam.req;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MsgUnreadReq extends PageReq{

    /**
     * 用户ID
     */

    private Integer uid;

    /**
     * 回复我的
     */

    private Integer reply;

    /**
     * @我的
     */

    private Integer at;

    /**
     * 收到的赞
     */

    private Integer love;

    /**
     * 系统通知
     */

    private Integer system;

    /**
     * 我的消息
     */

    private Integer whisper;

    /**
     * 动态
     */

    private Integer dynamic;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getReply() {
        return reply;
    }

    public void setReply(Integer reply) {
        this.reply = reply;
    }

    public Integer getAt() {
        return at;
    }

    public void setAt(Integer at) {
        this.at = at;
    }

    public Integer getLove() {
        return love;
    }

    public void setLove(Integer love) {
        this.love = love;
    }

    public Integer getSystem() {
        return system;
    }

    public void setSystem(Integer system) {
        this.system = system;
    }

    public Integer getWhisper() {
        return whisper;
    }

    public void setWhisper(Integer whisper) {
        this.whisper = whisper;
    }

    public Integer getDynamic() {
        return dynamic;
    }

    public void setDynamic(Integer dynamic) {
        this.dynamic = dynamic;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", uid=").append(uid);
        sb.append(", reply=").append(reply);
        sb.append(", at=").append(at);
        sb.append(", love=").append(love);
        sb.append(", system=").append(system);
        sb.append(", whisper=").append(whisper);
        sb.append(", dynamic=").append(dynamic);
        sb.append("]");
        return sb.toString();
    }
}
