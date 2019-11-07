package com.llc.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by catt on 2019/10/29.
 */
//消息的消费者
public class JmsConsumer {
    private static final String ACTIVE_URL = "tcp://192.168.245.130:61616";
    private static final String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws Exception {
        System.out.println("这里是2号消费者");
        //1.按照给定的url创建连接工厂，这个构造器采用默认的用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVE_URL);
        //2.通过连接工厂连接connection 和启动
        Connection connection = activeMQConnectionFactory.createConnection();
        //启动
        connection.start();
        //3.创建会话 session
        //两个参数，第一个事务，第二个签收方式
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地（两种：队列/主题  这里用队列）
        Queue queue = session.createQueue(QUEUE_NAME);
        //5.创建消息的消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);

        /*//6.接收消息
       //6.1 第一种接收消息的方法是receive（）
         //同步阻塞方式reveive() 空参数的receive方法是阻塞，有参数的为等待时间，等待时间一过就不再阻塞等待了
         //订阅者或消费者使用messageconsumer 的receive()方法接收消息，receive在接收之前一直阻塞
        //
        while (true){
            //这里是textMessage是因为消息发送者是textmessage,接收处理的也应该是这个类型的消息
            TextMessage textMessage = (TextMessage) messageConsumer.receive();
            if (messageConsumer != null){
                System.out.println("****消费者的消息："+textMessage.getText());
            }else {
                break;
            }
        }*/
       //6.2 第二种接收方式是通过监听的方式来消费消息
        //通过异步阻塞的方式消费消息
        //通过messageconsumer的setmessagelister注册一个监听器
        //当有消息发送过来时，系统自动调用messagelistener的onMessage方法处理消息
        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message != null && message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("****消费者的消息： "+textMessage.getText());
                    } catch (Exception e) {
                    	e.printStackTrace();
                    }
                }
            }
        });
        //保证控制台不灭 不然activemq连上就关掉连接了
        System.in.read();
        //关闭连接，从下往上关
        messageConsumer.close();
        session.close();
        connection.close();

    }
}
