package com.cyxx.plcgateway.mapper;



import com.cyxx.plcgateway.model.entry.IotDeviceEntry;
import com.cyxx.plcgateway.model.entry.IotGatewayEntry;
import com.cyxx.plcgateway.model.entry.IotTemplateEntry;

import java.util.List;

/**
 * common mapper
 */
public interface CommonMapper {


    List<IotGatewayEntry> getIotGateways();

    List<IotDeviceEntry> getIotDevices(String gatewayName);

    List<IotTemplateEntry> getTemplate(String templateName);
}
