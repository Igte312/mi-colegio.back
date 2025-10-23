package com.micolegio.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ClaudeConfig {

    @Value("${claude.api.key}")
    private String key;

    @Value("${claude.api.url}")
    private String url;

    @Value("${claude.api.version}")
    private String version;

    @Value("${claude.api.model}")
    private String model;

    @Value("${claude.max.tokens}")
    private Integer maxTokens;
}
