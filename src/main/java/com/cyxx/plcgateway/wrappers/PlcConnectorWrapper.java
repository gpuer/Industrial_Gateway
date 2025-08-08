package com.cyxx.plcgateway.wrappers;

import com.cyxx.plcgateway.mapper.CommonMapper;
import com.cyxx.plcgateway.model.entry.IotGatewayEntry;
import com.cyxx.plcgateway.model.entry.IotTemplateEntry;
import com.cyxx.plcgateway.utils.BytesTempReader;
import com.github.s7connector.api.DaveArea;
import com.github.s7connector.api.S7Connector;
import com.github.s7connector.api.factory.S7ConnectorFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Getter
@Slf4j
public class PlcConnectorWrapper {

    private final IotGatewayEntry gateway;
    private S7Connector connector;
    @Resource
    private CommonMapper mapper;

    public PlcConnectorWrapper(IotGatewayEntry gateway) {
        this.gateway = gateway;
        init();
    }

    public synchronized void init() {
        if (gateway == null) {
            log.error("PlcConnectorWrapper 配置为空，无法初始化！");
            return;
        }
        connector = S7ConnectorFactory.buildTCPConnector()
                .withHost(gateway.getHost())
                .withPort(gateway.getPort() != null ? gateway.getPort() : 102)
                .withRack(gateway.getRack())
                .withSlot(gateway.getSlot())
                .build();
        log.info("PLC [{}] 初始化成功", gateway.getName());
    }

    public synchronized void reconnectIfNeeded() throws IOException {
        log.warn("PLC [{}] 已断开，尝试重连...", gateway.getName());
        destroy();
        init();
    }

    public synchronized void destroy() throws IOException {
        if (connector != null) {
            connector.close();
            log.info("PLC [{}] 已关闭", gateway.getName());
        }
    }





    public Object templateRead(int offset, int len,int db, List<IotTemplateEntry> registerItems) {


        try {
            log.info("开始读取PLC数据：DB={}, Offset={}, Len={}", db, offset, len);
            byte[] buffer = connector.read(DaveArea.DB, db, len, offset);
            log.info("PLC返回数据（Hex）: {}", buffer != null ? bytesToHex(buffer) : "null");
            if (buffer == null || buffer.length < len) {
                String msg = String.format("PLC读取失败，数据为空或长度不足: DB%d Offset%d Len%d", db, offset, len);
                log.error(msg);
                throw new RuntimeException(msg);
            }

            BytesTempReader reader = new BytesTempReader();
            return reader.parseRegistersToMapS7(buffer, registerItems);
        } catch (Exception e) {
            String msg = String.format("PLC读取异常：DB%d Offset%d Len%d -> %s", db, offset, len, e.getMessage());
            throw new RuntimeException(msg, e);
        }
    }

    // 辅助方法：字节数组转十六进制字符串
    private String bytesToHex(byte[] bytes) {
        if (bytes == null) return "null";
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }

}
