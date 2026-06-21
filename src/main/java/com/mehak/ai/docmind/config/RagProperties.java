package com.mehak.ai.docmind.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "docmind.rag")
public record RagProperties(
        int chunkSize,
        int chunkOverlap,
        int topK,
        double minScore
) {
}
