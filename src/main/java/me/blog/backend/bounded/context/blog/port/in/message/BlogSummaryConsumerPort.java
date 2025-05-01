package me.blog.backend.bounded.context.blog.port.in.message;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface BlogSummaryConsumerPort {
    void consume(ConsumerRecord<String, String> record);
}
