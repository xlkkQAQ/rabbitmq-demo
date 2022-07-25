package com.xlkk.exchange.topic;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xlkk.utils.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author xlkk
 * @date 2022/7/25 0025 16:32
 */
public class ReceiveLogTopic1 {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"topic",true);
        channel.queueDeclare("Q1",true,false,false,null);
        channel.queueBind("Q1",EXCHANGE_NAME,"*.orange.*");
        System.out.println("等待接收消息....");
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("接收到的消息是："+new String(message.getBody(),"UTF-8"));
            System.out.println("接受队列：Q1"+"绑定键:"+message.getEnvelope().getRoutingKey());
        };
        CancelCallback cancelCallback = (consumerTag)->{};
        channel.basicConsume("Q1",deliverCallback,cancelCallback);
    }
}
