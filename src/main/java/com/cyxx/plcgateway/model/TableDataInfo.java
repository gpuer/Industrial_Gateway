package com.cyxx.plcgateway.model;

import lombok.Data;

import java.util.List;

@Data
public class TableDataInfo {
    /** 总记录数 */
    private long total;

    /** 列表数据 */
    private List<?> rows;

    /** 消息状态码 */
    private int code;

    /** 消息内容 */
    private String msg;

    public TableDataInfo() {}

    public TableDataInfo(List<?> list, long total) {
        this.rows = list;
        this.total = total;
    }
}
