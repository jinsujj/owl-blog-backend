package me.blog.backend.bounded.context.history.port.in;

public interface historyUseCase {
    void saveIPInformation(String ipAddress);
    long getTodayCount();
    long getTotalCount();
}
