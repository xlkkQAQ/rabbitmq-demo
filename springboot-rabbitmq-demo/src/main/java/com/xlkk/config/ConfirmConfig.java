package com.xlkk.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xlkk
 * @date 2022/7/29 0029 15:32
 * @Description: 发布确认高级
 */
@Configuration
public class ConfirmConfig {
    /**
     * 交换机
     */
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    /**
     * 备份交换机
     */
    public static final String BACK_UP_EXCHANGE_NAME = "back.up.exchange";
    /**
     * 确认队列
     */
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    /**
     * 备份队列
     */
    public static final String BACK_UP_QUEUE_NAME = "back.up.queue";
    /**
     * 报警队列
     */
    public static final String WARNING_QUEUE_NAME = "warning.queue";
    /**
     * routingKey
     */
    public static final String ROUTING_KEY = "key1";

    /**
     * 确认交换机
     * @return DirectExchange
     */
    @Bean
    public DirectExchange confirmExchange(){
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                .withArgument("alternate-exchange",BACK_UP_EXCHANGE_NAME)
                .build();
//        return new DirectExchange(CONFIRM_EXCHANGE_NAME,true,false);
    }

    @Bean
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    @Bean
    public Binding queueBindingExchange(
            @Qualifier("confirmQueue") Queue confirmQueue,
            @Qualifier("confirmExchange")DirectExchange confirmExchange
    ){
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(ROUTING_KEY);
    }

    /**
     * 备份交换机声明
     * @return FanoutExchange
     */
    @Bean
    public FanoutExchange backupExchange(){
        return new FanoutExchange(BACK_UP_EXCHANGE_NAME,true,false);
    }

    @Bean
    public Queue backupQueue(){
        return QueueBuilder.durable(BACK_UP_QUEUE_NAME).build();
    }

    @Bean
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    /**
     * 备份队列绑定备份交换机
     * @param backupQueue
     * @param backupExchange
     * @return Binding
     */
    @Bean
    public Binding backupQueueBindingbackupExchange(
            @Qualifier("backupQueue") Queue backupQueue,
            @Qualifier("backupExchange") FanoutExchange backupExchange
    ){
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    /**
     * 报警队列绑定备份交换机
     * @param warningQueue
     * @param backupExchange
     * @return Binding
     */
    @Bean
    public Binding warningQueueBindingbackupExchange(
            @Qualifier("warningQueue") Queue warningQueue,
            @Qualifier("backupExchange") FanoutExchange backupExchange
    ){
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }


}
