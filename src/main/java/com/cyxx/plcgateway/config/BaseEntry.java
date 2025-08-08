package com.cyxx.plcgateway.config;

import com.baomidou.mybatisplus.annotation.TableField;


public class BaseEntry {

    @TableField(exist = false)
    private Integer pageNum;
    @TableField(exist = false)
    private Integer pageSize;

}
