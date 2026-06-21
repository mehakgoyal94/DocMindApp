package com.mehak.ai.docmind.service;


import com.mehak.ai.docmind.config.RagProperties;
import com.mehak.ai.docmind.model.ChatResponse;
import com.mehak.ai.docmind.model.SourceReference;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagQueryService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;
    private final RagProperties properties;

    public RagQueryService(
            VectorStore vectorStore,
            ChatClient.Builder chatClientBuilder,
            RagProperties properties
    ) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
        this.properties = properties;
    }

    public ChatResponse answer(String question) {
        long start = System.currentTimeMillis();

        List<Document> matches = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(properties.topK())
                        .similarityThreshold(properties.minScore())
                        .build()
        );

        if (matches.isEmpty()) {
            return new ChatResponse(
                    "I don't know based on the available documents.",
                    List.of(),
                    System.currentTimeMillis() - start
            );
        }

        String context = matches.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n---\n\n"));

        String prompt = """
                You are DocMind, a grounded knowledge assistant.

                Answer the user's question using only the provided context.
                If the answer is not present in the context, say:
                "I don't know based on the available documents."

                Context:
                %s

                Question:
                %s
                """.formatted(context, question);

        String answer = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        List<SourceReference> sources = matches.stream()
                .map(doc -> new SourceReference(
                        String.valueOf(doc.getMetadata().get("documentName")),
                        String.valueOf(doc.getId()),
                        extractScore(doc),
                        preview(doc.getText())
                ))
                .toList();

        return new ChatResponse(
                answer,
                sources,
                System.currentTimeMillis() - start
        );
    }

    private double extractScore(Document document) {
        Object score = document.getMetadata().get("score");
        if (score instanceof Number number) {
            return number.doubleValue();
        }
        return 0.0;
    }

    private String preview(String text) {
        if (text == null) {
            return "";
        }
        return text.length() <= 200 ? text : text.substring(0, 200) + "...";
    }
}
