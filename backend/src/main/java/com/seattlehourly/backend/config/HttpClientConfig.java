// src/main/java/com/seattlehourly/backend/config/HttpClientConfig.java
package com.seattlehourly.backend.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpClientConfig {

    @Bean
    public RestTemplate redditRestTemplate(RestTemplateBuilder builder) {
        return builder
                .defaultHeader("User-Agent", "SeattleHourlyBot/0.1 (by u/Nearby_Eye4135)")
                .build();
    }

    @Bean
    public RestTemplate emptyRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
