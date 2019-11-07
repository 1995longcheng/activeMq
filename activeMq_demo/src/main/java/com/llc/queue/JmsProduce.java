package com.llc.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by catt on 2019/10/29.
 */
public class JmsProduce {
    //linux上部署的activemq的ip+activemq端口号，
    public static final String ACTIVEMQ_URL = "tcp://192.168.245.130:61616";
    public static final String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException {
        //1.按照给定的url创建连接工程，这个构造器采用默认的用户名和密码
        ActiveMQConnectionFactory ActiveMQConnectionFactory  = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂连接connection 和启动
        Connection Connection = ActiveMQConnectionFactory.createConnection();
        //启动
        Connection.start();
        //3.创建会话 session
        //两个参数，第一个事务，第二个签收
        Session session = Connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地（两种：队列/主题  这里用队列）
        Queue Queue = session.createQueue(QUEUE_NAME);
        //5.创建消息的生产者
        MessageProducer messageProducer = session.createProducer(Queue);
        //非持久化消息 和持久化消息演示
        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);//持久化 默认开启，如果开启就会将数据存入文件或数据库中   NON_PERSISTENT 不开启持久化

        //6.通过messageProducer 产生3条消息发送到消息队列中
        for (int i = 1;i <= 3 ;i++){
            //7.创建文本对象消息
            TextMessage textMessage = session.createTextMessage("msg-- " + i);
            messageProducer.send(textMessage);
        }
        //9.关闭资源，从下往上关闭
        messageProducer.close();
        session.close();
        Connection.close();
        System.out.println("***消息发送到MQ完成");

    }
}
