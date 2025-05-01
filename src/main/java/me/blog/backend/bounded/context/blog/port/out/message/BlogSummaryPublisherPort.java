package me.blog.backend.bounded.context.blog.port.out.message;

import me.blog.backend.bounded.context.blog.domain.event.SummaryRequest;

public interface BlogSummaryPublisherPort {
    void publish(SummaryRequest event);
}
