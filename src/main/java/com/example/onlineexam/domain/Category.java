package com.example.onlineexam.domain;

public class Category {
    private String mcId;

    private String scId;

    private String mcName;

    private String scName;

    private String descr;

    private String rcmTag;

    public String getMcId() {
        return mcId;
    }

    public void setMcId(String mcId) {
        this.mcId = mcId;
    }

    public String getScId() {
        return scId;
    }

    public void setScId(String scId) {
        this.scId = scId;
    }

    public String getMcName() {
        return mcName;
    }

    public void setMcName(String mcName) {
        this.mcName = mcName;
    }

    public String getScName() {
        return scName;
    }

    public void setScName(String scName) {
        this.scName = scName;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getRcmTag() {
        return rcmTag;
    }

    public void setRcmTag(String rcmTag) {
        this.rcmTag = rcmTag;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", mcId=").append(mcId);
        sb.append(", scId=").append(scId);
        sb.append(", mcName=").append(mcName);
        sb.append(", scName=").append(scName);
        sb.append(", descr=").append(descr);
        sb.append(", rcmTag=").append(rcmTag);
        sb.append("]");
        return sb.toString();
    }
}