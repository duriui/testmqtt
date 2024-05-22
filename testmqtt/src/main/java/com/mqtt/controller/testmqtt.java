package com.mqtt.controller;



import com.alibaba.fastjson.JSONArray;
import com.mqtt.FileUtils.ExcleToJson;
import com.mqtt.MqttUtils.MqttConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @Description: 测试mqtt消息推送
 * @Author: DR
 * @Date: 2024/3/21 18:33
 */

@RestController
@RequestMapping("/test/mqtt")
public class testmqtt {

    @Autowired
    private MqttConfiguration mqttConfiguration;

    @GetMapping("/sendMessage")
    public String sendMessage(@RequestParam("topic") String topic,
                              @RequestParam("message") String message) {

        JSONArray objects = ExcleToJson.excleToJson();

        boolean publish = mqttConfiguration.publish(topic, objects.toJSONString());
        if (publish) {
            return "ok";
        }
        return "no";
    }

}

