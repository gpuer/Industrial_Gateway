package com.cyxx.plcgateway.model.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("iot_device_template")
public class IotTemplateEntry {

    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("field_name")
    private String fieldName;
    private String type; // float / int32
    private Integer length; // 寄存器个数
    private Float scale;
    @TableField("word_swap")
    private Integer wordSwap;  // 是否需要 word 交换
    @TableField("byte_swap")
    private Integer byteSwap;  // 是否需要 word 交换
    @TableField("\"offset\"")
    private int offset;
    @TableField("template_name")
    private String templateName;

    @TableField(exist = false)
    private Integer pageNum;
    @TableField(exist = false)
    private Integer pageSize;
}
