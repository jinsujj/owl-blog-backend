package me.blog.backend.bounded.context.blog.adapter.in.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.domain.event.SummaryResult;
import me.blog.backend.bounded.context.blog.port.in.SummaryUseCase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlogSummaryConsumer {
    private final SummaryUseCase summaryUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "summary-response", groupId = "summary-listener-group")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            SummaryResult result = objectMapper.readValue(record.value(), SummaryResult.class);
            summaryUseCase.consumeSummary(result);
        } catch (Exception e) {
            System.err.println("❌ Error parsing summary-response: " + e.getMessage());
        }
    }
}
