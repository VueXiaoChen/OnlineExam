package com.example.onlineexam.resp;

import java.util.List;

public class PageResp<T> {
    private long total;

    private List<T> data;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return data;
    }

    public void setList(List<T> data) {
        this.data = data;
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer("PageResp{");
        sb.append("total=").append(total);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
