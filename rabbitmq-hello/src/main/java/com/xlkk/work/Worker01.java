package com.xlkk.work;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xlkk.utils.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 一个工作线程
 * @author xlkk
 * @date 2022/7/22 0022 9:13
 */
public class Worker01 {
    public static final String QUEUE_NAME = "hello";
    public static void main(String[] args) throws IOException, TimeoutException {
        new Thread(()->{
            Channel channel = null;
            try {
                channel = RabbitMQUtil.getChannel();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }
            DeliverCallback deliverCallback = (consumerTag,delivery)->{
                String message =new String(delivery.getBody());
                System.out.println("A接受到的消息："+message);
            };
            CancelCallback cancelCallback = (consumerTag)->{
                System.out.println("消息被取消");
            };

            System.out.println("A等待接受消息......");
            try {
                channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        },"A").start();

        new Thread(()->{
            Channel channel = null;
            try {
                channel = RabbitMQUtil.getChannel();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }
            DeliverCallback deliverCallback = (consumerTag,delivery)->{
                String message =new String(delivery.getBody());
                System.out.println("B接受到的消息："+message);
            };
            CancelCallback cancelCallback = (consumerTag)->{
                System.out.println("消息被取消");
            };

            System.out.println("B等待接受消息......");
            try {
                channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        },"B").start();
    }
}
