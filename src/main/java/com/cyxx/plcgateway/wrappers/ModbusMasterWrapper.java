package com.cyxx.plcgateway.wrappers;

import com.cyxx.plcgateway.model.entry.IotGatewayEntry;
import com.cyxx.plcgateway.model.entry.IotTemplateEntry;
import com.cyxx.plcgateway.utils.BytesTempReader;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ModbusMasterWrapper {

    @Getter
    private final IotGatewayEntry gateway;
    private ModbusMaster master;

    private final ModbusFactory factory = new ModbusFactory();

    public ModbusMasterWrapper(IotGatewayEntry gateway) {
        this.gateway = gateway;
        init();
    }

    public synchronized void init() {
        if (gateway == null) {
            log.error("ModbusMasterWrapper 配置为空，无法初始化！");
            return;
        }

        IpParameters params = new IpParameters();
        params.setHost(gateway.getHost());
        params.setPort(gateway.getPort() != null ? gateway.getPort() : 502);
        params.setEncapsulated(false);

        master = factory.createTcpMaster(params, true);

        try {
            master.init();
            log.info("ModbusMaster [{}] 初始化成功", gateway.getName());
        } catch (ModbusInitException e) {
            log.error("ModbusMaster [{}] 初始化失败: {}", gateway.getName(), e.getMessage());
        }
    }

    public synchronized void reconnectIfNeeded() {
        if (master == null || !master.isInitialized()) {
            log.warn("ModbusMaster [{}] 已断开，尝试重连...", gateway.getName());
            destroy();
            init();
        }
    }

    public synchronized void destroy() {
        if (master != null) {
            master.destroy();
            log.info("ModbusMaster [{}] 已销毁", gateway.getName());
        }
    }

    public synchronized short[] readHoldingRegisters(int offset, int len) throws Exception {
        reconnectIfNeeded();
        ReadHoldingRegistersRequest request =
                new ReadHoldingRegistersRequest(gateway.getSlaveId(), offset, len);
        ReadHoldingRegistersResponse response =
                (ReadHoldingRegistersResponse) master.send(request);

        if (response.isException()) {
            throw new ModbusTransportException(
                    "Modbus 异常响应: " + response.getExceptionMessage());
        }
        return response.getShortData();
    }


    public Object templateRead(int offset, int len, List<IotTemplateEntry> registerItems) {
        try {
            short[] rawData = this.readHoldingRegisters(offset, len);
            byte[] bytes = BytesTempReader.convertShortsToBytes(rawData);
            BytesTempReader reader = new BytesTempReader();
            return reader.parseRegistersToMap(bytes, registerItems);

        } catch (Exception e) {
            reconnectIfNeeded();
            e.printStackTrace();
            throw new RuntimeException("MODBUS模板数据读取失败: ", e);
        }
    }

    public String getName() {
        return gateway.getName();
    }
}