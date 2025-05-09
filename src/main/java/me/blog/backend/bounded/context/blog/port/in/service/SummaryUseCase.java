package me.blog.backend.bounded.context.blog.port.in.service;

import me.blog.backend.bounded.context.blog.domain.event.SummaryResult;

public interface SummaryUseCase {
    void publishSummary(long blogId);
    void consumeSummary(SummaryResult result);
}
