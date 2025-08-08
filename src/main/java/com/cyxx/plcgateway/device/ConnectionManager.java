package com.cyxx.plcgateway.device;

import com.cyxx.plcgateway.mapper.CommonMapper;
import com.cyxx.plcgateway.wrappers.ModbusMasterWrapper;
import com.cyxx.plcgateway.model.entry.IotGatewayEntry;
import com.cyxx.plcgateway.wrappers.PlcConnectorWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ConnectionManager {

    @Autowired
    private CommonMapper commonMapper;

    @Getter
    private final Map<String, PlcConnectorWrapper> plcConnectorMap = new ConcurrentHashMap<>();
    @Getter
    private final Map<String, ModbusMasterWrapper> modbusMasterMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        loadGateways();
    }

    private void loadGateways() {
        List<IotGatewayEntry> gateways = commonMapper.getIotGateways();
        for (IotGatewayEntry gw : gateways) {
            if ("PLCS7".equalsIgnoreCase(gw.getType())) {
                connectPlc(gw);
            } else if ("MODBUS".equalsIgnoreCase(gw.getType())) {
                connectModbus(gw);
            }
        }
    }

    private void connectPlc(IotGatewayEntry gw) {
        try {
            PlcConnectorWrapper plcConnectorWrapper = new PlcConnectorWrapper(gw);
            plcConnectorMap.put(gw.getName(), plcConnectorWrapper);
            log.info("PLC连接成功 [{}]", gw.getName());
        } catch (Exception e) {
            log.error("PLC连接失败 [{}]: {}", gw.getName(), e.getMessage());
        }
    }

    private void connectModbus(IotGatewayEntry gw) {
        try {
            ModbusMasterWrapper master = new ModbusMasterWrapper(gw);
            modbusMasterMap.put(gw.getName(), master);
            log.info("Modbus连接成功 [{}]", gw.getName());
        } catch (Exception e) {
            log.error("Modbus连接失败 [{}]: {}", gw.getName(), e.getMessage());
        }
    }



    @PreDestroy
    public void closeAll() {
        log.info("DeviceCollectService 正在关闭所有连接...");

        for (Map.Entry<String, PlcConnectorWrapper> entry : plcConnectorMap.entrySet()) {
            try {
                entry.getValue().destroy();
                log.info("已关闭 PLC [{}]", entry.getKey());
            } catch (Exception e) {
                log.warn("关闭 PLC [{}] 时出错: {}", entry.getKey(), e.getMessage());
            }
        }

        for (Map.Entry<String, ModbusMasterWrapper> entry : modbusMasterMap.entrySet()) {
            try {
                entry.getValue().destroy();
                log.info("已关闭 Modbus [{}]", entry.getKey());
            } catch (Exception e) {
                log.warn("关闭 Modbus [{}] 时出错: {}", entry.getKey(), e.getMessage());
            }
        }
        log.info("所有连接已关闭");
    }


    public void reloadConnections() {
        log.info("重新加载所有网关连接");
        closeAll();
        plcConnectorMap.clear();
        modbusMasterMap.clear();
        loadGateways();
    }


    public PlcConnectorWrapper getPlcConnector(String name) { return plcConnectorMap.get(name); }
    public ModbusMasterWrapper getModbusMaster(String name) { return modbusMasterMap.get(name); }




}