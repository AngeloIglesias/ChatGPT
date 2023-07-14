package com.example.chatgpt.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneOffset;

@Configuration
public class I18nConfig {
    @Bean
    public ZoneOffset utcZoneOffset() {
        return ZoneOffset.ofHours(+2);
    }
}
