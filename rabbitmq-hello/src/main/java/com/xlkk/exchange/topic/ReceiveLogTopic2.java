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
public class ReceiveLogTopic2 {
    public static final String EXCHANGE_NAME = "topic_logs";
    /**
     * 绑定关系如下；
     * Q1->
     *  (*.orange.*)
     * Q2->
     *  (*.*.rabbit)
     *  (lazy.#)
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"topic",true);
        channel.queueDeclare("Q2",true,false,false,null);
        channel.queueBind("Q2",EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind("Q2",EXCHANGE_NAME,"lazy.#");
        System.out.println("等待接收消息....");
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("接收到的消息是："+new String(message.getBody(),"UTF-8"));
            System.out.println("接受队列：Q2"+"绑定键:"+message.getEnvelope().getRoutingKey());
        };
        CancelCallback cancelCallback = (consumerTag)->{};
        channel.basicConsume("Q2",deliverCallback,cancelCallback);
    }
}
