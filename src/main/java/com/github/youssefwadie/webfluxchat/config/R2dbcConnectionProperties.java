package com.github.youssefwadie.webfluxchat.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.datasource.r2dbc")
public class R2dbcConnectionProperties {
    private String username;
    private String password;
    private String database;
    private String driver;
    private String protocol;
}
