package com.xlkk.consumer;

import com.xlkk.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author xlkk
 * @date 2022/7/29 0029 16:32
 * @Description:
 */
@Slf4j
@Component
public class Consumer {
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMsg(Message message){
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("接收到的队列confirm.queue消息:{}",msg);
    }
}
