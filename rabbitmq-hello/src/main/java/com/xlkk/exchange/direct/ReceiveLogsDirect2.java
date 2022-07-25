package com.xlkk.exchange.direct;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xlkk.utils.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author xlkk
 * @date 2022/7/25 0025 15:29
 */
public class ReceiveLogsDirect2 {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"direct",true);
        //声明一个临时队列
        channel.queueDeclare("disk",true,false,false,null);
        channel.queueBind("disk",EXCHANGE_NAME,"error");
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("ReceiveLogsDirect2发送的消息是"+new String(message.getBody()));
        };
        CancelCallback callback = (consumerTag)->{};
        channel.basicConsume("disk",deliverCallback,callback);
    }

}
