package me.blog.backend.bounded.context.blog.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blog.backend.bounded.context.blog.domain.event.SummaryRequest;
import me.blog.backend.bounded.context.blog.domain.event.SummaryResult;
import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.port.in.SummaryUseCase;
import me.blog.backend.bounded.context.blog.port.out.cache.BlogCachePort;
import me.blog.backend.bounded.context.blog.port.out.message.BlogEventPublisherPort;
import me.blog.backend.bounded.context.blog.port.out.repository.BlogRepositoryPort;
import me.blog.backend.common.exception.BlogNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService implements SummaryUseCase {
    private final BlogRepositoryPort blogRepository;
    private final BlogEventPublisherPort eventPublisher;
    private final BlogCachePort blogCache;

    @Override
    public void publishSummary(long blogId){
        Optional<BlogEntity> blog = blogRepository.findById(blogId);

        if(blog.isPresent()){
            SummaryRequest event = new SummaryRequest(blogId);
            eventPublisher.publish(event);
            return;
        }

        throw new BlogNotFoundException(String.valueOf(blogId) + " blog_id not found");
    }

    @Transactional
    @Override
    public void consumeSummary(SummaryResult result) {
        // 후처리 로직 (ex. DB 저장, 로그, 알림 등)
        log.info("Received summary:");
        log.info("🆔 Blog ID: {}", result.blogId());
        log.info("🧠 Summary: {}", result.summary());
        log.info("⏱️ Elapsed: {} sec", result.elapsedTimeSec());

        BlogEntity blog = blogRepository.findById(result.blogId())
            .orElseThrow(() -> new BlogNotFoundException(result.blogId().toString() + " not found"));

        blog.setSummary(result.summary());
        blogCache.putAll();
    }
}
