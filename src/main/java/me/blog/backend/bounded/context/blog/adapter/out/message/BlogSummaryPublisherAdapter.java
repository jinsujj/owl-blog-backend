package me.blog.backend.bounded.context.blog.adapter.out.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blog.backend.bounded.context.blog.domain.event.SummaryRequest;
import me.blog.backend.bounded.context.blog.port.out.message.BlogSummaryPublisherPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogSummaryPublisherAdapter implements BlogSummaryPublisherPort {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(SummaryRequest event) {
        try{
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("summary-request", json);
            log.info("Published blog created event: {}", event);
        } catch(Exception e){
            log.error("Failed to publish summary-request event", e);
        }
    }
}
