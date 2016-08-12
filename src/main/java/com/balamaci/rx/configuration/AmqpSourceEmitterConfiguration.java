package com.balamaci.rx.configuration;

import com.balamaci.rx.Receiver;
import com.google.gson.JsonObject;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import rx.Observable;

import java.io.UnsupportedEncodingException;


/**
 * @author Serban Balamaci
 */
@Configuration
@PropertySource("classpath:application.properties")
@Profile("amqp")
public class AmqpSourceEmitterConfiguration {

    private final static String queueName = "logstash-processing-queue";

    @Value("${amqp.host}")
    private String host;

    @Value("${amqp.port}")
    private Integer port;


    @Bean
    ConnectionFactory connectionFactory() {
        ConnectionFactory cf = new CachingConnectionFactory(host, port);

        return cf;
    }

    @Bean
    RabbitAdmin rabbitAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
        rabbitAdmin.declareQueue(queue());
        rabbitAdmin.declareBinding(bindQueueFromExchange(queue(), exchange()));
        return rabbitAdmin;
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);

        return container;
    }

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }


    DirectExchange exchange() {
        return new DirectExchange("logstash");
    }

    private Binding bindQueueFromExchange(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("my_app");
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver,
                new MessageConverter() {
            public Message toMessage(Object o, MessageProperties messageProperties)
                    throws MessageConversionException {
                throw new RuntimeException("Unsupported");
            }

            public String fromMessage(Message message) throws MessageConversionException {
                try {
                    return new String(message.getBody(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("UnsupportedEncodingException");
                }
            }
        });
        messageListenerAdapter.setDefaultListenerMethod("receive"); //the method in our Receiver class
        return messageListenerAdapter;
    }


    @Bean
    Receiver receiver() {
        return new Receiver();
    }

    @Bean(name = "events")
    public Observable<JsonObject> eventsStream() {
        return receiver().getPublishSubject();
    }

}
