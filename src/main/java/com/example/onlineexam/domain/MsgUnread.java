package com.example.onlineexam.domain;

public class MsgUnread {
    private Integer uid;

    private Integer reply;

    private Integer at;

    private Integer love;

    private Integer system;

    private Integer whisper;

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