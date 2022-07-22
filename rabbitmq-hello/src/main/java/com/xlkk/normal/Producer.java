package com.xlkk.normal;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者-发消息
 * @author xlkk
 * @date 2022/7/21 0021 15:49
 */
public class Producer {
    public static final String QUEUE_NAME="hello";

    //发消息
    public static void main(String[] args) {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
//        connectionFactory.setConnectionTimeout(60000);
        //工厂ip-连接rabbitmp

        connectionFactory.setHost("49.235.100.175");
//        connectionFactory.setPort(5672);
//        connectionFactory.setVirtualHost("admin");
        //设置用户名和密码
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        //创建连接
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();

            //获取信道
            Channel channel = connection.createChannel();

            /**
             * 创建一个队列
             * 1.队列名称
             * 2.队列里面的消息是否持久化（默认在内存中），而持久化就是存储在磁盘中
             * 3.该队列是否只供一个消费者进行消费，是否进行消息共享
             * 4.是否自动删除，最后一个消费者端开连接以后，是否自动删除
             * 5.其他参数
             */

            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            //发消息
            String message = "hello xlkk";
            /**
             * 发送一个消息：
             * 1.表示发送到哪个交换机
             * 2.路由的key是哪一个-本次队列的名称
             * 3.其它参数
             * 4.发送的消息
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("消息发送完毕");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }


    }

}
