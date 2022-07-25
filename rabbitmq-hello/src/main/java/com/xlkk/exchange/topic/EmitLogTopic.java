package com.xlkk.exchange.topic;

import com.rabbitmq.client.Channel;
import com.xlkk.utils.RabbitMQUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author xlkk
 * @date 2022/7/25 0025 16:11
 */
public class EmitLogTopic {
    public static final String EXCHANGE_NAME = "topic_logs";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        /*
         * 绑定关系如下；
         * Q1->
         *  (*.orange.*)
         * Q2->
         *  (*.*.rabbit)
         *  (lazy.#)
         */
        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        bindingKeyMap.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        bindingKeyMap.put("quick.orange.fox","被队列 Q1 接收到");
        bindingKeyMap.put("lazy.brown.fox","被队列 Q2 接收到");
        bindingKeyMap.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        bindingKeyMap.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");
        for (Map.Entry<String, String> stringEntry : bindingKeyMap.entrySet()) {
            String routingKey = stringEntry.getKey();
            String message = stringEntry.getValue();
            channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发送消息："+message);
        }
    }
}
