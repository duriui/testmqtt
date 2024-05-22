package com.mqtt.MqttUtils;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.context.annotation.Configuration;


/**
 * @Description:
 * @Author: DR
 * @Date: 2024/3/22 13:27
 */

@Configuration
public class MqttCallBack implements MqttCallback {


    /**
     * 与服务器断开的回调
     */
    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("与服务器断开连接");
    }

    /**
     * 消息到达的回调
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        System.out.println(String.format("接收消息主题 : %s", topic));
        System.out.println(String.format("接收消息Qos : %d", message.getQos()));
        System.out.println(String.format("接收消息内容 : %s", new String(message.getPayload())));
        System.out.println(String.format("接收消息retained : %b", message.isRetained()));
    }

    /**
     * 消息发布成功的回调
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        IMqttAsyncClient client = token.getClient();
        System.out.println(client.getClientId() + "发布消息成功！");
    }

}

