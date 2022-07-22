package com.xlkk.work;

import com.rabbitmq.client.Channel;
import com.xlkk.utils.RabbitMQUtil;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 * @author xlkk
 * @date 2022/7/22 0022 9:30
 */
public class Task01 {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //进行队列声明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //从控制塔接受信息
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("发送消息完成："+message);
        }

    }


}
