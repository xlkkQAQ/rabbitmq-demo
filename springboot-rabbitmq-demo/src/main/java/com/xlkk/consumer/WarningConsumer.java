package com.xlkk.consumer;

import com.xlkk.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author xlkk
 * @date 2022/8/2 0002 14:44
 * @Description: 报警消费者
 */

@Component
@Slf4j
public class WarningConsumer {

    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message){
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.error("报警发现不可路由消息{}",msg);
    }



}
