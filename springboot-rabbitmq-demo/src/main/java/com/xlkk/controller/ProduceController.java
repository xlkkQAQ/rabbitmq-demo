package com.xlkk.controller;

import com.xlkk.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xlkk
 * @date 2022/7/29 0029 15:50
 * @Description: 生产者
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProduceController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public void sendMessage(@PathVariable String message){
        CorrelationData correlationData = new CorrelationData("1");

        rabbitTemplate.convertAndSend(
                ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.ROUTING_KEY,
                message,
                correlationData);

        log.info("发送消息:{}",message);

    }

}
