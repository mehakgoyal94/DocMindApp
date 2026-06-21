package com.mehak.ai.docmind.model;

import java.util.List;

public record ChatResponse(
        String answer,
        List<SourceReference> sources,
        long latencyMs
) {
}
