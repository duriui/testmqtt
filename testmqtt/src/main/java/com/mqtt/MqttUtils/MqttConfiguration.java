package com.mqtt.MqttUtils;


import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * @Description: json数据推送到mqtt
 * @Author: DR
 * @Date: 2024/3/22 11:10
 */

@Configuration
public class MqttConfiguration {

    @Value("${spring.mqtt.host}")
    String host;
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${spring.mqtt.clientId}")
    String clientId;
    @Value("${spring.mqtt.timeout}")
    int timeOut;
    @Value("${spring.mqtt.keepalive}")
    int keepAlive;
    @Value("${spring.mqtt.topic1}")
    String topic1;
    @Value("${spring.mqtt.topic2}")
    String topic2;

    /**
     * 客户端对象
     */
    private MqttClient client;

    /**
     * 在bean初始化后连接到服务器
     */
    @PostConstruct
    public void init() {
        this.connect();
    }

    /**
     * 断开连接
     */
    @PreDestroy
    public void disConnect() {
        try {
            client.disconnect();
            client.close();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端连接服务端
     */
    public void connect() {
        try {
            // 创建MQTT客户端对象
            client = new MqttClient(host, applicationName, new MemoryPersistence());
            // 连接设置
            MqttConnectOptions options = new MqttConnectOptions();
            // 是否清空session，设置false表示服务器会保留客户端的连接记录（订阅主题，qos）,客户端重连之后能获取到服务器在客户端断开连接期间推送的消息
            // 设置为true表示每次连接服务器都是以新的身份
            options.setCleanSession(true);
//            // 设置连接用户名
//            options.setUserName(username);
//            // 设置连接密码
//            options.setPassword(password.toCharArray());
            // 设置超时时间，单位为秒
            options.setConnectionTimeout(100);
            // 设置心跳时间 单位为秒，表示服务器每隔 1.5*20秒的时间向客户端发送心跳判断客户端是否在线
            options.setKeepAliveInterval(20);
            // 设置遗嘱消息的话题，若客户端和服务器之间的连接意外断开，服务器将发布客户端的遗嘱信息
            options.setWill("willTopic", (applicationName + "与服务器断开连接").getBytes(), 0, false);
            // 设置回调
            client.setCallback(new MqttCallBack());
            // 连接
            client.connect(options);
            // 订阅主题 (接受此主题的消息)
            this.subscribe("warn_topic", 2);
            this.subscribe("warn_topic2", 2);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发布消息
     *
     * @param topic
     * @param message
     */
    public boolean publish(String topic, String message) {
        MqttMessage mqttMessage = new MqttMessage();
        // 0：最多交付一次，可能丢失消息
        // 1：至少交付一次，可能消息重复
        // 2：只交付一次，既不丢失也不重复
        mqttMessage.setQos(2);
        // 是否保留最后一条消息
        mqttMessage.setRetained(false);
        // 消息内容
        mqttMessage.setPayload(message.getBytes());
        // 主题的目的地，用于发布/订阅信息
        MqttTopic mqttTopic = client.getTopic(topic);
        // 提供一种机制来跟踪消息的传递进度
        // 用于在以非阻塞方式（在后台运行）执行发布是跟踪消息的传递进度
        MqttDeliveryToken token;
        try {
            // 将指定消息发布到主题，但不等待消息传递完成，返回的token可用于跟踪消息的传递状态
            // 一旦此方法干净地返回，消息就已被客户端接受发布，当连接可用，将在后台完成消息传递。
            token = mqttTopic.publish(mqttMessage);
            token.waitForCompletion();
            return true;
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 订阅主题
     */
    public void subscribe(String topic, int qos) {
        try {
            client.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}

