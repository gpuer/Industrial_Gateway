package com.cyxx.plcgateway.redis;

import com.cyxx.plcgateway.config.RedisCache;
import com.cyxx.plcgateway.mqtt.MqttSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisQueryPush {

    private final MqttSenderService mqttSenderService;
    private final RedisCache redisCache;

    @Value("${spring.mqtt.queue-key}")
    private String queueKey;


    public RedisQueryPush(MqttSenderService mqttSenderService, RedisCache redisCache){
        this.mqttSenderService = mqttSenderService;
        this.redisCache = redisCache;
    }

    @Scheduled(fixedDelayString = "${scheduler.redis-query-delay-ms}")
    public void sendMqttFromQueue() {
        while(true){
            String payload = redisCache.leftPop(queueKey);
            if (payload == null) break;
            try {
                mqttSenderService.send(payload);
//                log.info("MQTT 发送成功: {}", payload);
            } catch (Exception e) {
                log.warn("MQTT 发送失败，重新入队: {}", e.getMessage());
                // 放回队尾重试
                redisCache.rightPush(queueKey,payload);
                break;
            }

            try {
                Thread.sleep(50); // 延迟 50 毫秒
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
