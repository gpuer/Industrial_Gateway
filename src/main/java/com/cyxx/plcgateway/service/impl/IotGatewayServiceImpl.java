package com.cyxx.plcgateway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyxx.plcgateway.mapper.IotGatewayMapper;
import com.cyxx.plcgateway.model.entry.IotGatewayEntry;
import com.cyxx.plcgateway.service.IIotGatewayService;
import org.springframework.stereotype.Service;

@Service
public class IotGatewayServiceImpl extends ServiceImpl<IotGatewayMapper, IotGatewayEntry> implements IIotGatewayService {
}
