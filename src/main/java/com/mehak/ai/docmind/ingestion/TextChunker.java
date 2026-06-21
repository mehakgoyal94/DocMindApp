package com.mehak.ai.docmind.ingestion;

import com.mehak.ai.docmind.config.RagProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TextChunker {

    private final RagProperties properties;

    public TextChunker(RagProperties properties) {
        this.properties = properties;
    }

    public List<String> chunk(String text) {
        String cleanText = text.replaceAll("\\s+", " ").trim();

        int chunkSize = properties.chunkSize();
        int overlap = properties.chunkOverlap();

        List<String> chunks = new ArrayList<>();

        int start = 0;
        while (start < cleanText.length()) {
            int end = Math.min(start + chunkSize, cleanText.length());
            chunks.add(cleanText.substring(start, end));

            if (end == cleanText.length()) {
                break;
            }

            start = Math.max(0, end - overlap);
        }

        return chunks;
    }
}
