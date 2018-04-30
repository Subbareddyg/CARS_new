package com.belk.car.integrations.pim;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.JmsTemplate;

public class JmsQueueSender {
    
    private JmsTemplate jmsTemplate;
    private Queue queue;
    
    public JmsQueueSender() {
        
    }

    public void setJmsTemplate(JmsTemplate t) {
        this.jmsTemplate = t;
    }

    public void setQueue(Queue q) {
        this.queue = q;
    }
    
    public void sendMessageXML(final String xml) {
        MessageCreator newMessage = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(xml);
            }
        };
        jmsTemplate.send(queue, newMessage);
    }
}
