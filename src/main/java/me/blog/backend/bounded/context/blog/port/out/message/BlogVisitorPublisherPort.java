package me.blog.backend.bounded.context.blog.port.out.message;

public interface BlogVisitorPublisherPort {
    void publish(String ipAddress, String blogId);
}
