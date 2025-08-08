package com.cyxx.plcgateway.config;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyxx.plcgateway.model.TableDataInfo;

import java.util.List;

public class BaseController {

    public <T> TableDataInfo getDataTable(Page<T> page) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        rspData.setTotal(page.getTotal());
        rspData.setRows(page.getRecords());
        return rspData;
    }

    public <T> TableDataInfo getDataTable(List<T> list, long total) {
        TableDataInfo rspData = new TableDataInfo(list, total);
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        return rspData;
    }

    public AjaxResult success(Object data)
    {
        return AjaxResult.success(data);
    }
}
