package com.github.youssefwadie.webfluxchat.sse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class MessageEventRouter {

    @Bean
    public RouterFunction<ServerResponse> messagesRoute(@NonNull MessageEventHandler handler) {
        return RouterFunctions.route()
                .GET("/sse", handler::getMessages)
                .POST("/sse", handler::sendMessage)
                .build();
    }
}
