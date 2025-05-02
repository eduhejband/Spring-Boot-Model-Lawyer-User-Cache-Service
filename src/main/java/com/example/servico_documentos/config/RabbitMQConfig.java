package com.example.servico_documentos.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue; // Corrigir o import
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CLIENTE_QUEUE = "cliente.criado.documentos";
    public static final String CLIENTE_EXCHANGE = "cliente.exchange";
    public static final String ROUTING_KEY = "cliente.criado";

    @Bean
    public Queue clienteQueue() {
        return new Queue(CLIENTE_QUEUE, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(CLIENTE_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue clienteQueue, TopicExchange exchange) {
        return BindingBuilder.bind(clienteQueue)
                .to(exchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}