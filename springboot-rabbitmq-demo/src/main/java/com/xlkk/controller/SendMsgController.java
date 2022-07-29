package com.xlkk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author xlkk
 * @date 2022/7/28 0028 16:22
 * 发送延迟消息
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * sendmsg
     * @param message
     */
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message){
        log.info("当前时间：{},发送一条信息给两个ttl队列：{}",new Date().toString(),message);
        rabbitTemplate.convertAndSend("X","XA","消息来自ttl为10s的队列："+message);
        rabbitTemplate.convertAndSend("X","XB","消息来自ttl为40s的队列："+message);
    }

    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message,@PathVariable String ttlTime){
        log.info("当前时间{},发送一条消息给一个时长是{}ms的TTL队列QC:{}",new Date().toString(),ttlTime,message);
        rabbitTemplate.convertAndSend("X","XC","发给C的msg"+message,(msg)->{
            //设置发送消息的过期时间
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }
}
