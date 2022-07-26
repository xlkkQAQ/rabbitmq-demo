package com.xlkk.deadqueue;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xlkk.utils.RabbitMQUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列--消费者2
 * @author xlkk
 * @date 2022/7/26 0026 22:44
 */
public class Consumer02 {

    /**
     * 死信队列
     */
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("Consumer2收到消息："+new String(message.getBody(),"UTF-8"));
        };
        CancelCallback cancelCallback = (consumerTag)->{};
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,cancelCallback);
    }

}
