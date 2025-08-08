package com.cyxx.plcgateway.device;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cyxx.plcgateway.config.RedisCache;
import com.cyxx.plcgateway.mapper.CommonMapper;
import com.cyxx.plcgateway.model.entry.IotTemplateEntry;
import com.cyxx.plcgateway.wrappers.ModbusMasterWrapper;
import com.cyxx.plcgateway.model.entry.IotDeviceEntry;
import com.cyxx.plcgateway.wrappers.PlcConnectorWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@EnableScheduling
public class CollectScheduler {

    @Autowired
    private ConnectionManager connectionManager;
    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private RedisCache redisCache;

    @Value("${spring.mqtt.queue-key}")
    private String queueKey;


    @Resource(name = "collectExecutor")
    private ThreadPoolTaskExecutor collectExecutor;
    @Scheduled(fixedDelayString = "${scheduler.plc-collect-delay-ms}")
    public void doCollect() {
        log.info("开始采集任务");
        connectionManager.getPlcConnectorMap().forEach((gwName, conn) -> {
            List<IotDeviceEntry> devices = commonMapper.getIotDevices(gwName);
            for (IotDeviceEntry dev : devices) {
                collectExecutor.submit(() -> {
                    PlcConnectorWrapper wrapper = connectionManager.getPlcConnector(gwName);
                    try {
                        List<IotTemplateEntry> registerItems = commonMapper.getTemplate(dev.getTemplateName());
                        Object obj = wrapper.templateRead(dev.getOffset(),dev.getLen(),dev.getDb(),registerItems);
                        pushDataByRedisQueue(dev,obj);
                    } catch (Exception e) {
                        log.error("PLC [{}] 设备 [{}] 异常: {}", gwName, dev.getDeviceName(), e.getMessage());
                        try {
                            wrapper.reconnectIfNeeded();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                sleepSilently();
            }
        });

        connectionManager.getModbusMasterMap().forEach((gwName, master) -> {
            List<IotDeviceEntry> devices = commonMapper.getIotDevices(gwName);
            for (IotDeviceEntry dev : devices) {
                ModbusMasterWrapper wrapper = connectionManager.getModbusMaster(gwName);
                collectExecutor.submit(() -> {
                    try {

                        List<IotTemplateEntry> registerItems = commonMapper.getTemplate(dev.getTemplateName());
                        if (registerItems == null) {
                            throw new IllegalArgumentException("未找到模板信息,"+dev.getDeviceName());
                        }
                        Object obj = wrapper.templateRead(dev.getOffset(),dev.getLen(),registerItems);
                        pushDataByRedisQueue(dev,obj);
                    } catch (Exception e) {
                        log.error("Modbus [{}] 设备 [{}] 异常: {}", gwName, dev.getDeviceName(), e.getMessage());
                    }
                });
                sleepSilently();
            }
        });
    }


    public void pushDataByRedisQueue(IotDeviceEntry dev,Object object){
        if (object == null) {
            return;
        }
        JSONObject jsonObject = (JSONObject) JSON.toJSON(object);
        jsonObject.put("deviceName", dev.getDeviceName());
        jsonObject.put("sn", dev.getSn());
        redisCache.rightPush(queueKey,jsonObject.toJSONString());
    }

    private void sleepSilently() {
        try {
            Thread.sleep(new Random().nextInt(100) + 50);
        } catch (InterruptedException ignored) {
        }
    }
}