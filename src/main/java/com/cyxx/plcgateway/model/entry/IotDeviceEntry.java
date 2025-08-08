package com.cyxx.plcgateway.model.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("iot_device")
public class IotDeviceEntry {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("device_name")
    private String deviceName;
    private String sn;
    @TableField("\"offset\"")
    private Integer offset;
    private Integer len;
    @TableField("template_name")
    private String templateName;
    @TableField("gateway_name")
    private String gatewayName;
    private Integer db;

    @TableField(exist = false)
    private Integer pageNum;
    @TableField(exist = false)
    private Integer pageSize;
}
