package com.github.youssefwadie.webfluxchat.sse;

import com.github.youssefwadie.webfluxchat.dto.ChatMessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class MessageEventHandler {
    private final Sinks.Many<ChatMessageDTO> messagesSinks;
    private final ObjectMapper objectMapper;

    public MessageEventHandler(@Qualifier("chatMessageSinks") Sinks.Many<ChatMessageDTO> messagesSinks,
                               ObjectMapper objectMapper) {
        this.messagesSinks = messagesSinks;
        this.objectMapper = objectMapper;
    }


    public Mono<ServerResponse> sendMessage(ServerRequest request) {
        final Mono<ChatMessageDTO> chatMessageMono = request.bodyToMono(ChatMessageDTO.class)
                .doOnSuccess(messagesSinks::tryEmitNext);

        return ServerResponse.ok()
                .body(chatMessageMono, ChatMessageDTO.class);
    }

    public Mono<ServerResponse> getMessages(@SuppressWarnings("unused") ServerRequest request) {
        Flux<String> messagesSource = messagesSinks.asFlux()
                .handle((chatMessage, sink) -> {
                    try {
                        String json = objectMapper.writeValueAsString(chatMessage);
                        sink.next(json);
                    } catch (JsonProcessingException e) {
                        sink.error(new RuntimeException(e));
                    }
                });

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(messagesSource, String.class);
    }

}
