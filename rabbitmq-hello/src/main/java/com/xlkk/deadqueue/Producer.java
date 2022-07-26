package com.xlkk.deadqueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.xlkk.utils.RabbitMQUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列-生产者
 * @author xlkk
 * @date 2022/7/26 0026 22:29
 */
public class Producer {
    /**
     * 普通交换机名称-对接的是生产者
     */
    public static final String EXCHANGE_NAME = "normal_exchange";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();

        //死信消息--设置ttl（存活时间）
        for (int i = 1; i <= 10; i++) {
            String message = "info "+i;
            //通过设置props来设置相关参数的设置--设置过期时间
            AMQP.BasicProperties basicProps = new AMQP.BasicProperties()
                    .builder().expiration("10000").build();
            channel.basicPublish(EXCHANGE_NAME,"zhangsan",null,message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
