package com.cyxx.plcgateway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyxx.plcgateway.mapper.IotTemplateMapper;
import com.cyxx.plcgateway.model.entry.IotTemplateEntry;
import com.cyxx.plcgateway.service.IIotTemplateService;
import org.springframework.stereotype.Service;

@Service
public class IotTemplateServiceImpl extends ServiceImpl<IotTemplateMapper, IotTemplateEntry> implements IIotTemplateService {
}
