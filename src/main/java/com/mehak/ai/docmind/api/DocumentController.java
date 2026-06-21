package com.mehak.ai.docmind.api;


import com.mehak.ai.docmind.model.DocumentStatusResponse;
import com.mehak.ai.docmind.service.DocumentIngestionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentIngestionService ingestionService;

    public DocumentController(DocumentIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DocumentStatusResponse upload(@RequestParam("file") MultipartFile file) {
        return ingestionService.ingest(file);
    }

    @GetMapping("/{documentId}/status")
    public DocumentStatusResponse status(@PathVariable String documentId) {
        return ingestionService.getStatus(documentId);
    }
}
