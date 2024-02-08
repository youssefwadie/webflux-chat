package com.github.youssefwadie.webfluxchat.repository;

import com.github.youssefwadie.webfluxchat.model.ChatMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatMessageRepository {
    Mono<ChatMessage> save(Mono<ChatMessage> chatMessage);
    default Mono<ChatMessage> save(ChatMessage chatMessage) {
        return save(Mono.just(chatMessage));
    }

    Flux<ChatMessage> saveAll(Flux<ChatMessage> chatMessageFlux);
    Flux<ChatMessage> findAll();

}
