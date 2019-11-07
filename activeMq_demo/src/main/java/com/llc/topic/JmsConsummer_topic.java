package com.llc.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by catt on 2019/10/30.
 */
public class JmsConsummer_topic {
    public static final String ACTIVEMQ_URL = "tcp://192.168.245.130:61616";
    public static final String TOPIC_NAME = "topic01";

    public static void main(String[] args) throws Exception {
        System.out.println("这里是1号消费者");
        //1.按照给定的url创建连接工厂，这个构造器采用默认的用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂连接connection和启动
        Connection connection = activeMQConnectionFactory.createConnection();
        //启动
        connection.start();
        //3.创建会话session 两个参数，第一个事务，第二个签收方式
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地（两种：队列/主题 这里用主题）
        Topic topic = session.createTopic(TOPIC_NAME);
        //5.创建消息的创建者
        MessageConsumer messageConsumer = session.createConsumer(topic);

        messageConsumer.setMessageListener(message -> {
            if (null != message && message instanceof TextMessage){
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("***消费者text的消息："+textMessage.getText());
                } catch (Exception e) {
                	e.printStackTrace();
                }
            }
            if (null != message && message instanceof MapMessage){
                MapMessage mapMessage = (MapMessage) message;
                try {
                    System.out.println("***消费者的map消息："+ mapMessage.getString("k1"));
                } catch (Exception e) {
                }
            }
        });

        //保证控制台不灭，不然activemq还没连上就关掉了连接
        System.in.read();
        messageConsumer.close();
        session.close();
        connection.close();

    }
}
