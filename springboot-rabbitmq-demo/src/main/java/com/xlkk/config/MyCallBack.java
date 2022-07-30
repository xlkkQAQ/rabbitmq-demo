package com.xlkk.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author xlkk
 * @date 2022/7/29 0029 16:53
 * @Description:
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback {
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
        if(ack==true){
            log.info("交换机已经收到Id为:{}的消息{}");
        }else {

        }
    }
}
