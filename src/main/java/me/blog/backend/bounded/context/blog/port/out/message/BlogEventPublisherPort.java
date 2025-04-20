package me.blog.backend.bounded.context.blog.port.out.message;

import me.blog.backend.bounded.context.blog.domain.event.SummaryRequest;

public interface BlogEventPublisherPort {
    void publish(SummaryRequest event);
}
