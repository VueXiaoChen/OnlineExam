package com.example.onlineexam.util;

public class JobParamVO {
    private long id;
    private String className;
    private String CronExpression;
    private String param;
    private Boolean status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCronExpression() {
        return CronExpression;
    }

    public void setCronExpression(String cronExpression) {
        CronExpression = cronExpression;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("JobParamVO{");
        sb.append("id=").append(id);
        sb.append(", className='").append(className).append('\'');
        sb.append(", CronExpression='").append(CronExpression).append('\'');
        sb.append(", param='").append(param).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
