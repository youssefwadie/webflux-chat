package com.github.youssefwadie.webfluxchat.repository;

import com.github.youssefwadie.webfluxchat.model.ChatMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ChatMessageRepositoryTest {
    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Test
    void save() {
        String sender = "youssef";
        String content = "Hello world";
        Instant createdAt = Instant.now();
        ChatMessage chatMessage = ChatMessage.builder()
                .sender(sender)
                .content(content)
                .createdAt(createdAt)
                .build();

        Mono<ChatMessage> savedChatMessageMono = chatMessageRepository.save(chatMessage);
        StepVerifier.create(savedChatMessageMono)
                .expectNextMatches(savedChatMessage -> {
                    assertNotNull(savedChatMessage.getId());
                    assertThat(savedChatMessage.getId()).isGreaterThan(0);
                    assertThat(savedChatMessage.getSender()).isEqualTo(sender);
                    assertThat(savedChatMessage.getContent()).isEqualTo(content);
                    assertThat(savedChatMessage.getCreatedAt()).isEqualTo(createdAt);
                    return true;
                })
                .verifyComplete();

    }

    @Test
    void findAll() {
        String sender = "youssef";
        String content = "Hello world";
        Instant createdAt = Instant.now();
        Flux<ChatMessage> chatMessageFlux = Flux.range(1, 100)
                .map(integer -> {
                    return ChatMessage.builder()
                            .sender(sender)
                            .content(content)
                            .createdAt(createdAt)
                            .build();
                });

        Flux<ChatMessage> savedChatMessageFlux = chatMessageRepository.saveAll(chatMessageFlux)
                .thenMany(chatMessageRepository.findAll());
        AtomicInteger count = new AtomicInteger();

        StepVerifier.create(savedChatMessageFlux)
                .thenConsumeWhile(savedChatMessage -> {
                    count.incrementAndGet();
                    assertNotNull(savedChatMessage.getId());
                    assertThat(savedChatMessage.getId()).isGreaterThan(0);
                    assertThat(savedChatMessage.getSender()).isEqualTo(sender);
                    assertThat(savedChatMessage.getContent()).isEqualTo(content);
                    assertThat(savedChatMessage.getCreatedAt()).isEqualTo(createdAt);
                    return true;
                })
                .verifyComplete();

        assertThat(count.get()).isEqualTo(100);
    }
}