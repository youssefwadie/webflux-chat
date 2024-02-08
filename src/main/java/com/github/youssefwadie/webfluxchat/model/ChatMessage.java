package com.github.youssefwadie.webfluxchat.model;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
public class ChatMessage {
    private Long id;
    private String sender;
    private String content;
    private Instant createdAt;
}
