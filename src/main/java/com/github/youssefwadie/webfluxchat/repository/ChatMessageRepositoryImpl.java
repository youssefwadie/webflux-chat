package com.github.youssefwadie.webfluxchat.repository;

import com.github.youssefwadie.webfluxchat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Repository

@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepository {
    private static final String INSERT_CHAT_MESSAGE_SQL = "INSERT INTO chat_messages(sender, content, created_at) VALUES (:sender, :content, :createdAt)";
    private static final String SELECT_ALL_CHAT_MESSAGES_SQL = "SELECT * FROM chat_messages";

    private final DatabaseClient databaseClient;

    private final TransactionalOperator rxtx;

    @Override
    public Mono<ChatMessage> save(Mono<ChatMessage> chatMessageMono) {
        return saveAll(chatMessageMono.flux()).singleOrEmpty();
    }

    @Override
    public Flux<ChatMessage> saveAll(Flux<ChatMessage> chatMessageFlux) {
        Flux<ChatMessage> savedChatMessageFlux = chatMessageFlux.flatMap(chatMessage -> {
            return databaseClient.sql(INSERT_CHAT_MESSAGE_SQL)
                    .filter((statement, next) -> statement.returnGeneratedValues("id").execute())
                    .bind("sender", chatMessage.getSender())
                    .bind("content", chatMessage.getContent())
                    .bind("createdAt", chatMessage.getCreatedAt())
                    .fetch()
                    .one()
                    .map(generatedValuesMap -> {
                        Long id = (Long) generatedValuesMap.get("id");
                        return ChatMessage.builder()
                                .id(id)
                                .sender(chatMessage.getSender())
                                .content(chatMessage.getContent())
                                .createdAt(chatMessage.getCreatedAt())
                                .build();

                    });
        });

        return rxtx.transactional(savedChatMessageFlux);
    }

    @Override
    public Flux<ChatMessage> findAll() {
        Flux<ChatMessage> chatMessageFlux = databaseClient.sql(SELECT_ALL_CHAT_MESSAGES_SQL)
                .fetch()
                .all()
                .map(this::mapObjectMap);

        return rxtx.transactional(chatMessageFlux);
    }


    private ChatMessage mapObjectMap(Map<String, Object> rowMap) {
        Long id = (Long) rowMap.get("id");
        String sender = (String) rowMap.get("sender");
        String content = (String) rowMap.get("content");
        Instant createdAt = ((LocalDateTime) rowMap.get("created_at")).atZone(ZoneId.systemDefault()).toInstant();

        return ChatMessage.builder()
                .id(id)
                .sender(sender)
                .content(content)
                .createdAt(createdAt)
                .build();
    }
}
