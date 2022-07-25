package com.xlkk.exchange.fanout;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xlkk.utils.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author xlkk
 * @date 2022/7/25 0025 14:20
 */
public class ReceiveLog1 {
    public static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout",true);
        //声明一个临时队列
        String queue = channel.queueDeclare().getQueue();
        /*
         * 绑定交换机与队列
         * 队列名称
         * 交换机名称
         * routingKey
         */
        channel.queueBind(queue,EXCHANGE_NAME,"");
        System.out.println("ReceiveLog1等待接受消息，把接收到的消息打印在屏幕上");
        //接受消息的回调
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("ReceiveLog1接收到的消息是："+new String(message.getBody()));
        };
        //取消接受消息的回调
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println("cancel");
        };
        channel.basicConsume(queue,true,deliverCallback,cancelCallback);

    }
}
