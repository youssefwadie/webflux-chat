package com.github.youssefwadie.webfluxchat.service;

import com.github.youssefwadie.webfluxchat.dto.ChatMessageDTO;
import com.github.youssefwadie.webfluxchat.model.ChatMessage;
import com.github.youssefwadie.webfluxchat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;


@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public Mono<Void> saveMessage(Mono<ChatMessageDTO> chatMessage) {
        return chatMessageRepository.save(chatMessage.map(this::toModel)).then();
    }

    @Override
    public Flux<ChatMessageDTO> findAll() {
        return chatMessageRepository.findAll().map(this::toDTO);
    }


    private ChatMessage toModel(ChatMessageDTO chatMessageDTO) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(chatMessageDTO.getSender());
        chatMessage.setContent(chatMessageDTO.getContent());
        chatMessage.setCreatedAt(Instant.now());
        return chatMessage;
    }
    private ChatMessageDTO toDTO(ChatMessage chatMessage) {
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setContent(chatMessage.getContent());
        chatMessageDTO.setSender(chatMessage.getSender());
        return chatMessageDTO;
    }

}
