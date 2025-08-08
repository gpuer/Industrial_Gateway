package com.cyxx.plcgateway.mqtt;


import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

/**
 * mqtt 消息处理类
 */
@Slf4j
@Component
public class MqttMessageHandler implements MessageHandler {


    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        String payload = (String) message.getPayload();
        System.out.println("Received message from topic " + topic + ": " + payload);
    }



}
