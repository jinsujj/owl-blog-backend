package me.blog.backend.bounded.context.blog.domain.event;

public record SummaryResult(Long blogId, String summary, double elapsedTimeSec) {}
