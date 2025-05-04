package me.blog.backend.bounded.context.history.port.in;

import me.blog.backend.bounded.context.history.domain.vo.CoordinateVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface historyUseCase {
    void saveIPInformation(String ipAddress, String blogId, LocalDateTime createdAt);
    long getTodayCount();
    long getTotalCount();
    List<CoordinateVO> getCoordinatesHistory(LocalDate from, LocalDate to);
}
