package com.github.youssefwadie.webfluxchat;

import com.github.youssefwadie.webfluxchat.config.R2dbcConnectionProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(R2dbcConnectionProperties.class)
public class WebfluxChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxChatApplication.class, args);
    }

}
