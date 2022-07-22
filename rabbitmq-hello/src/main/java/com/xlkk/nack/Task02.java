package com.xlkk.nack;

import com.rabbitmq.client.Channel;
import com.xlkk.utils.RabbitMQUtil;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author xlkk
 * @date 2022/7/22 0022 14:18
 */
public class Task02 {
    //队列名称
    public static final String QUEUE_NAME = "nack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //队列声明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //从控制台中输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("消息发送成功："+message);
        }
    }

}
