package com.xlkk.normal;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author xlkk
 * @date 2022/7/21 0021 16:26
 */
public class Consumer {
    public static final String QUEUE_NAME="hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //工厂ip-连接RabbitMQ
        connectionFactory.setHost("49.235.100.175");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");

        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();
        //声明
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println(message);
        };
        //取消消费时的回调
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println("消费消息被中断");
        };

        /**
         * 消费者接受消息
         * 1.队列名称
         * 2.消费成功之后是否要自动应答
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         *
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
