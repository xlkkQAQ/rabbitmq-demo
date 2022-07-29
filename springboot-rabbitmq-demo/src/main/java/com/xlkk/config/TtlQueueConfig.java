package com.xlkk.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * ttl队列
 * @author xlkk
 * @date 2022/7/28 0028 15:41
 */
@Slf4j
@Configuration
public class TtlQueueConfig {
    /**
     * 普通交换机名称
     */
    public static final String X_EXCHANGE = "X";
    /**
     * 死信交换机名称
     */
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";

    /**
     * 普通队列名称
     */
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";
    /**
     * 死信队列名称
     */
    public static final String QUEUE_DEAD_LETTER = "QD";

    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }


    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }


    @Bean("queueA")
    public Queue queueA(){
        Map<String, Object> arguments = new HashMap<>(3);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key","YD");
        //设置过期时间-单位 ms
        arguments.put("x-message-ttl",10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    @Bean("queueB")
    public Queue queueB(){
        Map<String, Object> arguments = new HashMap<>(3);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key","YD");
        //设置过期时间-单位 ms
        arguments.put("x-message-ttl",40000);

        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    @Bean("queueC")
    public Queue queueC(){
        Map<String, Object> arguments = new HashMap<>(3);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key","YD");
        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    @Bean("queueD")
    public Queue queueD(){
        return QueueBuilder.durable(QUEUE_DEAD_LETTER).build();
    }

    /**
     * 绑定
     */
    @Bean
    public Binding queueABindingX(@Qualifier("queueA")Queue queueA,
                                  @Qualifier("xExchange")DirectExchange xExchange){
        log.info("路过XA的绑定");
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }


    /**
     * （解决了一个bug），交换机应该写DirectExchange，而不是Exchange！！！
     * @param queueD
     * @param yExchange
     * @return
     */
    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }


}
