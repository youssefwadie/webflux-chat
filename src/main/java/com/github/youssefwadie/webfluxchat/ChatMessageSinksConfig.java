package com.github.youssefwadie.webfluxchat;

import com.github.youssefwadie.webfluxchat.dto.ChatMessageDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration(proxyBeanMethods = false)
public class ChatMessageSinksConfig {

    @Bean
    public Sinks.Many<ChatMessageDTO> chatMessageSinks() {
        return Sinks.many().multicast().directBestEffort();
    }
}

