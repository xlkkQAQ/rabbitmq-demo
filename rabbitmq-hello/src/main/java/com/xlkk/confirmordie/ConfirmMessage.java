package com.xlkk.confirmordie;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.xlkk.utils.RabbitMQUtil;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author xlkk
 * @date 2022/7/22 0022 16:14
 * 验证发布确认模式
 *  1.单个确认
 *  2.批量确认
 *  3.异步批量确认
 *  通过比较使用的时间，来确认哪一种方式最好
 */
public class ConfirmMessage {
    public static final int MESSAGE_COUNT = 1000;
    public static void main(String[] args) {
        //1.调取单个确认
//        publishIndividually();
        //2.调取批量确认
//        publishBatch();
        //3.调取异步批量确认
        publishAsync();
    }
    /**
     * 单个确认
     */
    public static void publishIndividually(){
        try {
            Channel channel = RabbitMQUtil.getChannel();
            //队列声明，随机获取
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName,true,false,false,null);
            //开启发布确认模式
            channel.confirmSelect();
            //记录开始时间
            long beginTime = System.currentTimeMillis();
            //批量发送消息
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = String.valueOf(i);
                channel.basicPublish("",queueName,null,message.getBytes());
                //单个消息，马上进行发布确认
                channel.waitForConfirms();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("发布1000个单独确认消息，一共耗时"+(endTime-beginTime)/1000+"s");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量确认发布
     */
    public static void publishBatch(){
        try {
            Channel channel = RabbitMQUtil.getChannel();
            //队列声明，随机获取
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName,true,false,false,null);
            //开启发布确认模式
            channel.confirmSelect();
            //记录开始时间
            long beginTime = System.currentTimeMillis();
            //批量确认消息发布的大小
            int batchSize = 100;

            //批量发送消息
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = String.valueOf(i);
                channel.basicPublish("",queueName,null,message.getBytes());
                //判断达到每100条，发布确认一次
                if(i%batchSize==0){
                    channel.waitForConfirms();
                }

            }

            long endTime = System.currentTimeMillis();
            System.out.println("发布1000个批量确认消息，一共耗时"+(endTime-beginTime)/1000+"s");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 异步确认发布
     */
    public static void publishAsync(){
        try {
            Channel channel = RabbitMQUtil.getChannel();
            //队列声明，随机获取
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName,true,false,false,null);
            //开启发布确认模式
            channel.confirmSelect();
            /**
             * 线程安全有序的哈希表，适用于高并发的情况下
             * 1.能够轻松地将序号与消息关联
             * 2.轻松地根据序号批量删除条目
             * 3.支持高并发
             */
            ConcurrentSkipListMap<Long,String> outstandingConfirm =
                    new ConcurrentSkipListMap<>();


            //记录开始时间
            long beginTime = System.currentTimeMillis();
            //准备一个消息监听器,监听哪些消息发送成功和失败来判断是否重新发送
            ConfirmCallback ackCallback = (deliveryTag,multiple)->{
                //批量删除
                if(multiple){
                    //2.删除已经确认的消息
                    ConcurrentNavigableMap<Long, String> confirmed =
                            outstandingConfirm.headMap(deliveryTag);
                    confirmed.clear();
                }else {
                    outstandingConfirm.remove(deliveryTag);
                }
                System.out.println("确认的消息："+deliveryTag);
            };
            /**
             * deliveryTag:消息的编号
             * multiple:是否批量发布
             */
            ConfirmCallback nackCallback = (deliveryTag,multiple)->{
                //打印未确认的消息
                System.out.println("未确认的消息："+deliveryTag);
            };
            channel.addConfirmListener(ackCallback,nackCallback);
            //批量发送消息
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = "message"+i;
                channel.basicPublish("",queueName,null,message.getBytes());
                //1.记录所有要发送的消息
                outstandingConfirm.put(channel.getNextPublishSeqNo(), message);
            }

            long endTime = System.currentTimeMillis();
            System.out.println("发布1000个异步确认消息，一共耗时"+(endTime-beginTime)+"ms");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
