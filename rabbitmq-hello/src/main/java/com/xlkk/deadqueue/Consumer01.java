package com.xlkk.deadqueue;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xlkk.utils.RabbitMQUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列实战-消费者1
 * @author xlkk
 * @date 2022/7/26 0026 21:59
 */
public class Consumer01 {
    /**
     * 普通交换机
     */
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    /**
     * 死信交换机
     */
    public static final String DEAD_EXCHANGE = "dead_exchange";
    /**
     *  普通队列
     */
    public static final String NORMAL_QUEUE = "normal_queue";
    /**
     * 死信队列
     */
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //声明交换机类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE,"direct");
        channel.exchangeDeclare(DEAD_EXCHANGE,"direct");
        HashMap<String, Object> arguments = new HashMap<>();
        //设置过期时间、死信交换机
//        arguments.put("x-message-ttl",10000);
        //设置普通队列的最大长度为6--超过6个会进入死信队列
//        arguments.put("x-max-length",6);
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信交换机与死信队列之间的routingKey
        arguments.put("x-dead-letter-routing-key","lisi");


        //声明普通队列--Map(String,Object) arguments
        channel.queueDeclare(NORMAL_QUEUE,true,false,false,arguments);
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE,true,false,false,null);
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");
        System.out.println("等待接收消息...");

        DeliverCallback deliverCallback = (consumerTag,message)->{
            String msg = new String(message.getBody(), "UTF-8");
            if (msg.equals("info 5")){
                System.out.println(msg+"被Consumer01拒收了");
                //拒收并且不放回普通队列
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println("Consumer1收到消息："+msg);
                //如果正常应答(手动应答)
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }

        };
        CancelCallback cancelCallback = (consumerTag)->{};
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,cancelCallback);
    }

}
