package com.example.onlineexam.req;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoryReq extends PageReq{

    /**
     * 主分区ID
     */

    private String mcId;

    /**
     * 子分区ID
     */

    private String scId;

    /**
     * 主分区名称
     */

    private String mcName;

    /**
     * 子分区名称
     */

    private String scName;

    /**
     * 描述
     */
    private String descr;

    /**
     * 推荐标签
     */
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
