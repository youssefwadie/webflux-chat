package com.github.youssefwadie.webfluxchat.ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Map;

@Configuration(proxyBeanMethods = false)
public class MessageWebSocketConfig {

    @Bean
    public SimpleUrlHandlerMapping chatHandlerMapping(MessageWebSocketHandler handler) {
        return new SimpleUrlHandlerMapping(Map.of("/ws", handler), -1);
    }
}
