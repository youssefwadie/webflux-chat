package com.github.youssefwadie.webfluxchat.ws;

import com.github.youssefwadie.webfluxchat.dto.ChatMessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.SynchronousSink;

@Component
public class MessageWebSocketHandler implements WebSocketHandler {
    private final Sinks.Many<ChatMessageDTO> messagesSinks;
    private final ObjectMapper objectMapper;

    public MessageWebSocketHandler(@Qualifier("chatMessageSinks") Sinks.Many<ChatMessageDTO> messagesSinks,
                                   ObjectMapper objectMapper) {
        this.messagesSinks = messagesSinks;
        this.objectMapper = objectMapper;
    }

    @Override
    @NonNull
    public Mono<Void> handle(@NonNull WebSocketSession session) {
        Mono<Void> inbound = session.receive()
                .doOnNext(event -> {
                    try {
                        String json = event.getPayloadAsText();
                        ChatMessageDTO message = objectMapper.readValue(json, ChatMessageDTO.class);
                        messagesSinks.tryEmitNext(message);
                    } catch (JsonProcessingException ignored) {

                    }
                })
                .then();

        Flux<WebSocketMessage> source = messagesSinks.asFlux()
                .handle((ChatMessageDTO chatMessage, SynchronousSink<WebSocketMessage> webSocketMessageSink) -> {
                    try {
                        String json = objectMapper.writeValueAsString(chatMessage);
                        WebSocketMessage webSocketMessage = session.textMessage(json + "\n");
                        webSocketMessageSink.next(webSocketMessage);
                    } catch (JsonProcessingException e) {
                        webSocketMessageSink.error(new RuntimeException(e));
                    }
                });

        Mono<Void> outbound = session.send(source);
        return Mono.zip(inbound, outbound).then();
    }

}
