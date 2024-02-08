package com.github.youssefwadie.webfluxchat.service;

import com.github.youssefwadie.webfluxchat.dto.ChatMessageDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatMessageService {
    Mono<Void> saveMessage(Mono<ChatMessageDTO> chatMessage);
    Flux<ChatMessageDTO> findAll();
}
