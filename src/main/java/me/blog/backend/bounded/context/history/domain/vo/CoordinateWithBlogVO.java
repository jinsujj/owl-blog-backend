package me.blog.backend.bounded.context.history.domain.vo;

import java.time.LocalDateTime;

public record CoordinateWithBlogVO(
        String blogTitle,
        Long blogId,
        String country,
        String city,
        String ip,
        LocalDateTime createdAt,
        String lat,
        String lon
){}