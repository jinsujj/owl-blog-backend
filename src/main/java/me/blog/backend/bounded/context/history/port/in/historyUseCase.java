package me.blog.backend.bounded.context.history.port.in;

import me.blog.backend.bounded.context.history.domain.vo.CoordinateWithBlogVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface historyUseCase {
    void saveIPInformation(String ipAddress, Long blogId, LocalDateTime createdAt);
    long getTodayCount();
    long getTotalCount();
    List<CoordinateWithBlogVO> getCoordinatesHistoryWithAIpAddress(LocalDate from, LocalDate to, String ipAddress);
}
