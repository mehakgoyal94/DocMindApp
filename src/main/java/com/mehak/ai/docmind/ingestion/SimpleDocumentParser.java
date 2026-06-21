package com.mehak.ai.docmind.ingestion;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class SimpleDocumentParser {

    private final Tika tika = new Tika();

    public String parse(MultipartFile file) {
        try {
            return tika.parseToString(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse document: " + file.getOriginalFilename(), e);
        } catch (TikaException e) {
            throw new RuntimeException(e);
        }
    }
}
