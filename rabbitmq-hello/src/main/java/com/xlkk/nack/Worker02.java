package com.xlkk.nack;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xlkk.utils.RabbitMQUtil;
import com.xlkk.utils.SleepUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 取消自动应答：消息在手动应答时是不丢失，放回队列中重新消费
 * @author xlkk
 * @date 2022/7/22 0022 14:08
 */
public class Worker02 {
    public static final String QUEUE_NAME = "nack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        new Thread(()->{
            try {
                Channel channel = RabbitMQUtil.getChannel();
                System.out.println("A等待接受消息处理，时间较短");
                DeliverCallback deliverCallback = (consumerTag,delivery)->{
                    String message = new String(delivery.getBody());
                    //模拟处理时间
                    SleepUtil.sleep(1);
                    System.out.println("A接受到的消息："+message);
                    //手动应答
                    /**
                     * 1.消息的标记（用来标识消息）
                     * 2.是否批量应答(如果批量应答可能会造成其他消息还没处理就被应答)
                     */
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                };
                CancelCallback callback = (var1)->{
                    System.out.println("A取消应答消息");
                };
                //采用手动应答
                boolean autoAck = false;
                //设置预取值
                int prefetchCount = 5;
                channel.basicQos(prefetchCount);
                channel.basicConsume(QUEUE_NAME,autoAck,deliverCallback,callback);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }

        },"A").start();

        new Thread(()->{
            try {
                Channel channel = RabbitMQUtil.getChannel();
                System.out.println("B等待接受消息处理，时间较长");
                DeliverCallback deliverCallback = (consumerTag,delivery)->{
                    String message = new String(delivery.getBody());
                    //模拟处理时间
                    SleepUtil.sleep(10);
                    System.out.println("B接受到的消息："+message);
                    //手动应答
                    /**
                     * 1.消息的标记（用来标识消息）
                     * 2.是否批量应答(如果批量应答可能会造成其他消息还没处理就被应答)
                     */
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                };
                CancelCallback callback = (var1)->{
                    System.out.println("B取消应答消息");
                };
                //采用手动应答
                boolean autoAck = false;
                //设置预取值
                int prefetchCount = 2;
                channel.basicQos(prefetchCount);
                channel.basicConsume(QUEUE_NAME,autoAck,deliverCallback,callback);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }

        },"B").start();

    }
}
