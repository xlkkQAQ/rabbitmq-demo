package com.xlkk.controller;

import com.xlkk.config.DelayedQueueConfig;
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

    /**
     * 自定义时长-发送过期消息
     * @param message
     * @param ttlTime
     */
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message,@PathVariable String ttlTime){
        log.info("当前时间{},发送一条消息给一个时长是{}ms的TTL队列QC:{}",new Date().toString(),ttlTime,message);
        rabbitTemplate.convertAndSend("X","XC","发给C的msg"+message,(msg)->{
            //设置发送消息的过期时间
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    /**
     * 基于自定义交换机-延迟发送消息
     * @param message
     * @param delayedTime
     */
    @GetMapping("/sendDelayedMsg/{message}/{delayedTime}")
    public void delayedSendMsg(@PathVariable String message,@PathVariable Integer delayedTime){
        log.info("当前时间{},发送一条时长是{}ms的消息给延迟队列delayedqueue:{}",new Date().toString(),delayedTime,message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY,
                "发给C的msg"+message,
                (msg)->{
            //设置发送消息的延迟时长 ms
            msg.getMessageProperties().setDelay(delayedTime);
            return msg;
        });
    }

}

