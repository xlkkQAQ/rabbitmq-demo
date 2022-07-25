package com.xlkk.exchange.direct;

import com.rabbitmq.client.Channel;
import com.xlkk.utils.RabbitMQUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author xlkk
 * @date 2022/7/25 0025 15:31
 */
public class DirectLogs {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");

        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            System.out.println("输入你要发送的消息：");
            String message = scanner.next();
            System.out.println("输入你要发送的routingKey:");
            String routingKey = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes(StandardCharsets.UTF_8));
            if(routingKey.equals("info")||routingKey.equals("warning")){
                System.out.println("发送给ReceiveLogDirect1的消息："+message);
            }else if(routingKey.equals("error")){
                System.out.println("发送给ReceiveLogDirect2的消息："+message);
            }
//            System.out.println("生产者发出消息："+message);
        }

    }
}
