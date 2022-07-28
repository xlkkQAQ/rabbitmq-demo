package com.xlkk.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author xlkk
 * @date 2022/7/28 0028 16:29
 * 队列TTL的消费者
 */
@Slf4j
@Component
public class DeadLetterConsumer {
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) throws UnsupportedEncodingException {
        String msg =new String(message.getBody(),"UTF-8");
        log.info("来过消费者");
        log.info("当前时间：{},收到死信队列的消息：{}",new Date().toString(),msg);

    }


}
