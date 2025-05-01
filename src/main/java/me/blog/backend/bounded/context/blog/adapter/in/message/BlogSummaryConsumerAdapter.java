package me.blog.backend.bounded.context.blog.adapter.in.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blog.backend.bounded.context.blog.domain.event.SummaryResult;
import me.blog.backend.bounded.context.blog.port.in.message.BlogSummaryConsumerPort;
import me.blog.backend.bounded.context.blog.port.in.service.SummaryUseCase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogSummaryConsumerAdapter implements BlogSummaryConsumerPort {
    private final SummaryUseCase summaryUseCase;
    private final ObjectMapper objectMapper;

    @Override
    @KafkaListener(topics = "summary-response", groupId = "summary-listener-group")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            SummaryResult result = objectMapper.readValue(record.value(), SummaryResult.class);
            summaryUseCase.consumeSummary(result);
        } catch (Exception e) {
            log.error("❌ Error parsing summary-response: " ,e);
        }
    }
}
