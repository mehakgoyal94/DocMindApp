package com.mehak.ai.docmind.api;


import com.mehak.ai.docmind.model.ChatRequest;
import com.mehak.ai.docmind.model.ChatResponse;
import com.mehak.ai.docmind.service.RagQueryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final RagQueryService ragQueryService;

    public ChatController(RagQueryService ragQueryService) {
        this.ragQueryService = ragQueryService;
    }

    @PostMapping("/query")
    public ChatResponse query(@Valid @RequestBody ChatRequest request) {
        return ragQueryService.answer(request.question());
    }
}
