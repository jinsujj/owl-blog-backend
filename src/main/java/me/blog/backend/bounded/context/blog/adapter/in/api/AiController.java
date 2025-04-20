package me.blog.backend.bounded.context.blog.adapter.in.api;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.application.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AiController {
    private final AiService aiService;

    @PostMapping("/ai/summary/{id}")
    public ResponseEntity<Void> summaryBlog(@PathVariable Long id){
        aiService.publishSummary(id);
        return ResponseEntity.noContent().build();
    }
}
