package com.example.chatgpt.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneOffset;

@Configuration
public class I18NConfig {
    @Value("${timezone}")
    int timezone;
    @Bean
    public ZoneOffset utcZoneOffset() {
        return ZoneOffset.ofHours(timezone);
    }
}
