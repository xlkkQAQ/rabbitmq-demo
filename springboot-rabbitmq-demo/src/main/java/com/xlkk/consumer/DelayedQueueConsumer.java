package com.xlkk.consumer;

import com.xlkk.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author xlkk
 * @date 2022/7/29 14:24
 * @Description:
 * 消费者 基于插件的延迟消息
 */
@Slf4j
@Component
public class DelayedQueueConsumer {
    /**
     * 监听消息
     */
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelayedQueue(Message message){
      String msg = new String(message.getBody(), StandardCharsets.UTF_8);
      log.info("当前时间:{},收到延迟队列的消息:{}",new Date(),msg);
    }

}
