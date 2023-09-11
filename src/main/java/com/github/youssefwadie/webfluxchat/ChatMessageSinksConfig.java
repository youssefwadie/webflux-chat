package com.github.youssefwadie.webfluxchat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration(proxyBeanMethods = false)
public class ChatMessageSinksConfig {

    @Bean
    public Sinks.Many<ChatMessage> chatMessageSinks() {
        return Sinks.many().multicast().directBestEffort();
    }
}

