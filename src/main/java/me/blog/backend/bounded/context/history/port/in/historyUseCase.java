package me.blog.backend.bounded.context.history.port.in;

import java.time.LocalDateTime;

public interface historyUseCase {
    void saveIPInformation(String ipAddress, LocalDateTime createdAt);
    long getTodayCount();
    long getTotalCount();
}
