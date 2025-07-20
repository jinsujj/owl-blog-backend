package me.blog.backend.bounded.context.blog.adapter.in.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blog.backend.bounded.context.blog.adapter.out.database.BlogRepository;
import me.blog.backend.bounded.context.blog.port.in.message.BlogVisitorConsumerPort;
import me.blog.backend.bounded.context.history.application.service.GeolocationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogVisitorConsumerAdapter implements BlogVisitorConsumerPort {
    private final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final GeolocationService geolocationService;
    private final BlogRepository blogRepository;

    @Override
    @KafkaListener(topics = "ip-history", groupId = "ip-history-group")
    public void consume(ConsumerRecord<String, String> record) {
        String message = record.value();
        try{
            JsonNode jsonNode = objectMapper.readTree(message);
            String ipAddress = jsonNode.get("ipAddress").asText();
            String timestamp = jsonNode.get("timestamp").asText();
            Long blogId = Long.parseLong(jsonNode.get("blogId").asText());
            LocalDateTime createdTime = LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER);

            if(ipAddress != null && ipAddress.startsWith("192")) return;
            log.info("Received IP visit: ipAddress={}, timestamp={}", ipAddress, createdTime);

            try {
                geolocationService.saveIPInformation(ipAddress, blogId, createdTime);
            } catch (Exception uniqueEx) {
                log.warn("Duplicate geo-location entry ignored: ip={}, blogId={}, createdAt={}", ipAddress, blogId,createdTime);
            }
    
            blogRepository.findById(blogId).ifPresent(s-> {
                if(s.isPublished()) s.readCounting();
            });
        }
        catch (Exception e){
            log.error("Failed to process ip-history message: {}", message, e);
        }
    }
}
