package me.blog.backend.bounded.context.blog.adapter.out.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blog.backend.bounded.context.blog.port.out.message.BlogVisitorPublisherPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogVisitorPublisherAdapter implements BlogVisitorPublisherPort {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void publish(String ipAddress, String blogId) {
        Map<String, String> payload = new HashMap<>();
        String timestamp = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(TIMESTAMP_FORMATTER);

        payload.put("ipAddress", ipAddress);
        payload.put("blogId", blogId);
        payload.put("timestamp", timestamp);

        try {
            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send("ip-history", message);
            log.info("Published ip-history created event: {}", message);

        } catch (Exception e) {
            log.error("Kafka 메시지 직렬화 실패: " ,e);
        }
    }
}
