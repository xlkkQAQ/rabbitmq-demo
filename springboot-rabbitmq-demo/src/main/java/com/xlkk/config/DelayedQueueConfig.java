package com.xlkk.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置延迟交换机
 * @author xlkk
 * @date 2022/7/29 0029 13:56
 */
@Configuration
public class DelayedQueueConfig {

    /**
     * 队列
     */
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    /**
     * 交换机
     */
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    /**
     * routingKey
     */
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    /**
     * 声明交换机-基于插件
     * 自定义交换机
     * @return CustomExchange
     */
    @Bean
    public CustomExchange delayedExchange(){
        Map<String, Object> argument = new HashMap<>();
        //延迟类型
        argument.put("x-delayed-type","direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME,
                "x-delayed-message",
                true,
                false,
                argument
        );
    }

    /**
     * 声明队列
     * @return
     */
    @Bean
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE_NAME);
    }

    /**
     * 建立绑定关系
     * @param delayedQueue
     * @param delayedExchange
     * @return
     */
    @Bean
    public Binding delayedQueueBindingDelayedExchange(
            @Qualifier("delayedQueue") Queue delayedQueue,
            @Qualifier("delayedExchange")CustomExchange delayedExchange
    ){
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }


}
