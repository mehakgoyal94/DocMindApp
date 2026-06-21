package com.mehak.ai.docmind.model;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
        @NotBlank String question
) {}
