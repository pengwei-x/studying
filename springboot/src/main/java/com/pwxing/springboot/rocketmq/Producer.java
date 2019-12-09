package com.pwxing.springboot.rocketmq;


import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * @author: pengwei
 * @date: 2019/11/30
 * rocketMQ 消息
 * 提供者
 */
public class Producer implements Serializable {

    private static final long serialVersionUID = 555056252804119159L;

    public static void main(String[] args) {

        //创建一个消息生产者，并设置一个消息生产者组
        DefaultMQProducer producer = new DefaultMQProducer("producer_group2");
        //指定 NameServer 地址
        producer.setNamesrvAddr("127.0.0.1:9876");
        //初始化 Producer，整个应用生命周期内只需要初始化一次
        try {
            producer.start();
        } catch (MQClientException e) {
            System.out.println("Producer启动失败," + e);
        }


        for (int i = 0, size = 50; i < size; i++) {
//             public Message(String topic, String tags, byte[] body)
            String content = "你好";
            Message message = null;
            try {
                //创建一条消息对象，指定其主题、标签和消息内容
                message = new Message("producet_topic", "a_tag", content.getBytes(RemotingHelper.DEFAULT_CHARSET));
            } catch (UnsupportedEncodingException e) {
                System.out.println("消息封装失败," + e);
            }
            //发送消息并返回结果
            SendResult sendResult = null;
            try {
                sendResult = producer.send(message);
            } catch (MQClientException e) {
                e.printStackTrace();
            } catch (RemotingException e) {
                e.printStackTrace();
            } catch (MQBrokerException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("%s%n", sendResult);
        }

        // 一旦生产者实例不再被使用则将其关闭，包括清理资源，关闭网络连接等
        producer.shutdown();
    }


}

