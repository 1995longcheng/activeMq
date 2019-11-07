package com.llc.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by catt on 2019/10/30.
 */
public class JmsProduce_topic {
    private static final String ACTIVEMQ_URL = "tcp://192.168.245.130:61616";
    private static final String TOPIC_NAME = "topic01";

    public static void main(String[] args) throws Exception{
        //1.按照给定的url创建连接工厂，这个构建器采用默认的用户名密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂连接connection 和启动
        Connection connection = activeMQConnectionFactory.createConnection();
        //启动
        connection.start();
        //3.创建会话session 两个参数 第一个事务，第二个签收方式
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地（两种：队列/主题 这里是主题）
        Topic topic = session.createTopic(TOPIC_NAME);
        //5.创建消息的生产者
        MessageProducer messageProducer = session.createProducer(topic);

        for (int i = 1; i< 4;i++){
            //7.创建文件消息
            TextMessage textMessage = session.createTextMessage("topic_name--"+i);
            //8.通过messageproducer发布消息
            messageProducer.send(textMessage);

           //发送map键值对消息
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("k1","v1");
            messageProducer.send(mapMessage);

        }

        //9.关闭资源
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("**** topic_name 消息发送到MQ完成");

    }
}
