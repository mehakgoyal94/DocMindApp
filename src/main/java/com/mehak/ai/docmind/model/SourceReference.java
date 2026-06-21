package com.mehak.ai.docmind.model;

public record SourceReference(
        String documentName,
        String chunkId,
        double score,
        String preview
) {
}
