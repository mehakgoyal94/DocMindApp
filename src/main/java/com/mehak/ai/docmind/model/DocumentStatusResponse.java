package com.mehak.ai.docmind.model;

public record DocumentStatusResponse(
        String documentId,
        String fileName,
        String status,
        int chunksCreated
) {
}
