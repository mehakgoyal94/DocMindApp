package com.mehak.ai.docmind;

import com.mehak.ai.docmind.config.RagProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RagProperties.class)
public class DocMindApp {

    public static void main(String[] args) {
        SpringApplication.run(DocMindApp.class, args);
    }
}
