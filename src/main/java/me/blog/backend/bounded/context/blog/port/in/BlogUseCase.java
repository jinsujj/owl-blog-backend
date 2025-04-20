package me.blog.backend.bounded.context.blog.port.in;

import me.blog.backend.bounded.context.blog.domain.vo.BlogVO;

import java.util.List;
import java.util.Map;

public interface BlogUseCase {
    BlogVO postBlog(String userId, String title, String content, String thumbnailUrl, String type);
    BlogVO updateBlog(Long id,String userId, String newTitle, String newContent, String thumbNailUrl);
    BlogVO updateBlogContent(Long id, String newContent);
    BlogVO publishBlog(Long id);
    BlogVO unPublishBlog(Long id);

    List<BlogVO> getAllBlogs();
    List<BlogVO> getAllBlogsByUser(String userId);
    BlogVO getBlogByType(String type);
    BlogVO getBlogById(Long id);
    Map<String, List<BlogVO>> getBlogGroupBySeries();
}

