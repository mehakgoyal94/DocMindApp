package com.mehak.ai.docmind.service;

import org.springframework.stereotype.Service;

import com.mehak.ai.docmind.ingestion.SimpleDocumentParser;
import com.mehak.ai.docmind.ingestion.TextChunker;
import com.mehak.ai.docmind.model.DocumentStatusResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DocumentIngestionService {

    private final SimpleDocumentParser parser;
    private final TextChunker chunker;
    private final VectorStore vectorStore;

    private final Map<String, DocumentStatusResponse> statusStore = new ConcurrentHashMap<>();

    public DocumentIngestionService(
            SimpleDocumentParser parser,
            TextChunker chunker,
            VectorStore vectorStore
    ) {
        this.parser = parser;
        this.chunker = chunker;
        this.vectorStore = vectorStore;
    }

    public DocumentStatusResponse ingest(MultipartFile file) {
        String documentId = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();

        String text = parser.parse(file);
        List<String> chunks = chunker.chunk(text);

        List<Document> documents = chunks.stream()
                .map(chunk -> new Document(
                        chunk,
                        Map.of(
                                "documentId", documentId,
                                "documentName", fileName == null ? "unknown" : fileName
                        )
                ))
                .toList();

        vectorStore.add(documents);

        DocumentStatusResponse response = new DocumentStatusResponse(
                documentId,
                fileName,
                "INGESTED",
                chunks.size()
        );

        statusStore.put(documentId, response);

        return response;
    }

    public DocumentStatusResponse getStatus(String documentId) {
        return statusStore.getOrDefault(
                documentId,
                new DocumentStatusResponse(documentId, "unknown", "NOT_FOUND", 0)
        );
    }
}