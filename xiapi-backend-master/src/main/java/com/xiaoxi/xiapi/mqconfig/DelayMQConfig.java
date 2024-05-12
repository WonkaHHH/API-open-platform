package com.xiaoxi.xiapi.mqconfig;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DelayMQConfig {

    @Bean("dlx_delay_exchange")
    public Exchange dlxDelayExchange(){
        return ExchangeBuilder.directExchange("dlx_delay_exchange").durable(true).build();
    }

    @Bean("dlx_delay_queue")
    public Queue dlxDelayQueue(){
        return QueueBuilder.durable("dlx_delay_queue").build();
    }

    @Bean
    public Binding bindDlxDelay(@Qualifier("dlx_delay_exchange") Exchange exchange,
                                @Qualifier("dlx_delay_queue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("dlx_delay").noargs();
    }

    @Bean("delay_exchange")
    public Exchange delayExchange(){
        return ExchangeBuilder.directExchange("delay_exchange").durable(true).build();
    }

    @Bean("delay_queue")
    public Queue delayQueue(){
        return QueueBuilder
                .durable("delay_queue")
//                .ttl(10 * 60 * 1000)
                .deadLetterExchange("dlx_delay_exchange")
                .deadLetterRoutingKey("dlx_delay")
                .build();
    }

    @Bean
    public Binding bindDelay(@Qualifier("delay_exchange") Exchange exchange,
                             @Qualifier("delay_queue") Queue queue){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("delay")
                .noargs();
    }
}
