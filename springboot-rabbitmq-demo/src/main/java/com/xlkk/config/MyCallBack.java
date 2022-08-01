package com.xlkk.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

/**
 * @author xlkk
 * @date 2022/7/29 0029 16:53
 * @Description:
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback {
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
        rabbitTemplate.setReturnsCallback(this);
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

    /**
     * 回退接口实现
     * 只有目的地不可达的时候，才会进行回退
     * @param returned the returned message and metadata.
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("消息{}被交换机{}给退回，退回的原因是:{},RoutingKey是:{}",
                new String(returned.getMessage().getBody(), StandardCharsets.UTF_8),
                returned.getExchange(),
                returned.getReplyText(),
                returned.getRoutingKey());
    }
}
