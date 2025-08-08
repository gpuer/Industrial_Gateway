package com.cyxx.plcgateway.redis;


import com.cyxx.plcgateway.mqtt.MqttMessageHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttInboundConfig {

    private final MqttPahoClientFactory mqttClientFactory;
    private final MqttMessageHandler mqttMessageHandler;


    @Value("${spring.mqtt.client-id}")
    private String clientId;

    @Value("${spring.mqtt.default-topic}")
    private String defaultTopic;

    public MqttInboundConfig(MqttPahoClientFactory mqttClientFactory,
                             MqttMessageHandler mqttMessageHandler) {
        this.mqttClientFactory = mqttClientFactory;
        this.mqttMessageHandler = mqttMessageHandler;
    }

    // ===== 接收配置 =====
    // 订阅消息适配器

    @Bean
    public MqttPahoMessageDrivenChannelAdapter inboundAdapter() {
        return new MqttPahoMessageDrivenChannelAdapter(clientId + "-inbound", mqttClientFactory, defaultTopic);
    }


    @Bean
    public IntegrationFlow mqttInFlow() {
        return IntegrationFlows.from(inboundAdapter())
                .handle(mqttMessageHandler) // 使用注入的 bean
                .get();
    }


    // ===== 发送配置 =====

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(
                clientId + "-outbound", mqttClientFactory
        );
        handler.setAsync(true);
        handler.setDefaultTopic(defaultTopic);
        return handler;
    }

    @Bean
    public IntegrationFlow mqttOutFlow() {
        return IntegrationFlows.from(mqttOutboundChannel())
                .handle(mqttOutbound())
                .get();
    }
}
