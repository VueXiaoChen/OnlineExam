package com.example.onlineexam.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class OperateLogResp {
    private Long logId;

    private String logUrl;

    private String logMethod;

    private String logIp;

    private String logStartdate;

    private String logEnddate;

    private String logResponsetime;

    private String logOperation;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date logOperationtime;

    private String logOptionuser;

    private String logParams;

    private String logResult;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public String getLogMethod() {
        return logMethod;
    }

    public void setLogMethod(String logMethod) {
        this.logMethod = logMethod;
    }

    public String getLogIp() {
        return logIp;
    }

    public void setLogIp(String logIp) {
        this.logIp = logIp;
    }

    public String getLogStartdate() {
        return logStartdate;
    }

    public void setLogStartdate(String logStartdate) {
        this.logStartdate = logStartdate;
    }

    public String getLogEnddate() {
        return logEnddate;
    }

    public void setLogEnddate(String logEnddate) {
        this.logEnddate = logEnddate;
    }

    public String getLogResponsetime() {
        return logResponsetime;
    }

    public void setLogResponsetime(String logResponsetime) {
        this.logResponsetime = logResponsetime;
    }

    public String getLogOperation() {
        return logOperation;
    }

    public void setLogOperation(String logOperation) {
        this.logOperation = logOperation;
    }

    public Date getLogOperationtime() {
        return logOperationtime;
    }

    public void setLogOperationtime(Date logOperationtime) {
        this.logOperationtime = logOperationtime;
    }

    public String getLogOptionuser() {
        return logOptionuser;
    }

    public void setLogOptionuser(String logOptionuser) {
        this.logOptionuser = logOptionuser;
    }

    public String getLogParams() {
        return logParams;
    }

    public void setLogParams(String logParams) {
        this.logParams = logParams;
    }

    public String getLogResult() {
        return logResult;
    }

    public void setLogResult(String logResult) {
        this.logResult = logResult;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", logId=").append(logId);
        sb.append(", logUrl=").append(logUrl);
        sb.append(", logMethod=").append(logMethod);
        sb.append(", logIp=").append(logIp);
        sb.append(", logStartdate=").append(logStartdate);
        sb.append(", logEnddate=").append(logEnddate);
        sb.append(", logResponsetime=").append(logResponsetime);
        sb.append(", logOperation=").append(logOperation);
        sb.append(", logOperationtime=").append(logOperationtime);
        sb.append(", logOptionuser=").append(logOptionuser);
        sb.append(", logParams=").append(logParams);
        sb.append(", logResult=").append(logResult);
        sb.append("]");
        return sb.toString();
    }
}