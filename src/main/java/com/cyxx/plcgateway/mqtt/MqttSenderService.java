package com.cyxx.plcgateway.mqtt;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class MqttSenderService {
    private final MessageChannel mqttOutboundChannel;
    @Value("${spring.mqtt.default-send-topic}")
    private String defaultTopic;

    public MqttSenderService(@Qualifier("mqttOutboundChannel") MessageChannel mqttOutboundChannel) {
        this.mqttOutboundChannel = mqttOutboundChannel;
    }

    public void send(String topic, String payload) {
        Message<String> message = MessageBuilder.withPayload(payload)
                .setHeader("mqtt_topic", topic)  // 也可用 MqttHeaders.TOPIC
                .build();
        mqttOutboundChannel.send(message);
    }

    public void send(String payload) {
        send(defaultTopic, payload);
    }
}
