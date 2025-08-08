package com.cyxx.plcgateway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyxx.plcgateway.mapper.IotDevicesMapper;
import com.cyxx.plcgateway.model.entry.IotDeviceEntry;
import com.cyxx.plcgateway.service.IIotDevicesService;
import org.springframework.stereotype.Service;

@Service
public class IotDevicesServiceImpl extends ServiceImpl<IotDevicesMapper, IotDeviceEntry> implements IIotDevicesService {
}
