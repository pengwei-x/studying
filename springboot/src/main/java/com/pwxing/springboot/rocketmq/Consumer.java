package com.pwxing.springboot.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * @author: pengwei
 * @date: 2019/11/30
 * rocketMq 消费者
 */
public class Consumer {
    public static void main(String[] args) {
        //创建一个消息消费者，并设置一个消息消费者组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer_group");
        //创建一个消息消费者，并设置一个消息消费者组
        consumer.setNamesrvAddr("127.0.0.1:9876");
        //设置 Consumer 第一次启动时从队列头部开始消费还是队列尾部开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //订阅指定 Topic 下的所有消息
        try {
            consumer.subscribe("producet_topic","*");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        //注册消息监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                if (list!=null){
                    for (MessageExt messageExt:list){
                        try {
                            System.out.println(new Date() + new String(messageExt.getBody(), "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 消费者对象在使用之前必须要调用 start 初始化
        try {
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        System.out.println("消息消费者已启动");
    }
}
