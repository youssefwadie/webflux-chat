package com.github.youssefwadie.webfluxchat.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.lang.NonNull;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@RequiredArgsConstructor
@Configuration
public class R2dbcConfig extends AbstractR2dbcConfiguration {
    private final R2dbcConnectionProperties connectionProperties;

    @Override
    @NonNull
    public ConnectionFactory connectionFactory() {
        val options = ConnectionFactoryOptions.builder()
                .option(DRIVER, connectionProperties.getDriver())
                .option(PROTOCOL, connectionProperties.getProtocol())
                .option(USER, connectionProperties.getUsername())
                .option(PASSWORD, connectionProperties.getPassword())
                .option(DATABASE, connectionProperties.getDatabase())
                .build();
        return ConnectionFactories.get(options);
    }

}
