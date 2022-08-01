package com.xlkk.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author xlkk
 * @date 2022/7/29 0029 16:53
 * @Description:
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback {
    /**
     * 注入Rabbitmq
     *
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 将回调接口注入到rabbitmq中
     */
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
    }
    /**
     * 交换机回调方法：
     * 1.发消息 交换机接收到消息   回调
     *  1.1 correlationData 用来保存回调消息的ID及相关信息
     *  1.2 交换机收到的消息 true
     *  1.3 cause null
     * 2.发消息 交换机接收失败    回调
     *  2.1 correlationData 用来保存回调消息的ID及相关信息
     *  2.2 交换机收到的消息 ack=false
     *  2.3 cause 引起失败的原因
     * @param correlationData correlation data for the callback.
     * @param ack true for ack, false for nack
     * @param cause An optional cause, for nack, when available, otherwise null.
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null? correlationData.getId() : "id为空";
        if(ack==true){
            log.info("交换机已经收到Id为:{}的消息",id);
        }else {
            //当出问题的时候ack=fasle
            log.info("交换机还未收到id为:{}的消息,由于原因:{}",id,cause);
        }

    }
}
