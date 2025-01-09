package me.blog.backend.domain.blog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlogService {
  private final BlogRepository blogRepository;

  public BlogService(BlogRepository blogRepository) {
    this.blogRepository = blogRepository;
  }

  @Transactional
  public BlogVO createBlog(String title, String content, List<String> attachments){
    BlogEntity blog = new BlogEntity(title, content, attachments, null, null,null);
    return BlogVO.fromEntity(blog);
  }

  @Transactional
  public BlogVO updateBlog(Long id, String newTitle, String newContent, List<String> newAttachments) {
    BlogEntity blogEntity = blogRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Blog not found"));
    blogEntity.setTitle(newTitle);
    blogEntity.setContent(newContent);
    blogEntity.setAttachments(newAttachments);
    blogEntity.setUpdatedAt(LocalDateTime.now());
    return BlogVO.fromEntity(blogRepository.save(blogEntity));
  }

  @Transactional(readOnly = true)
  public List<BlogVO> getAllBlogs() {
    return blogRepository.findAll().stream().map(BlogVO::fromEntity).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public BlogVO getBlogById(Long id) {
    return BlogVO.fromEntity(blogRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Blog not found")));
  }

  @Transactional
  public BlogVO publishBlog(Long id){
   BlogEntity blog = blogRepository.findById(id)
       .orElseThrow(() -> new RuntimeException("Blog not found"));

   blog.publish();
   return BlogVO.fromEntity(blogRepository.save(blog));
  }

  @Transactional
  public BlogVO unPublishBlog(Long id){
   BlogEntity blog = blogRepository.findById(id)
       .orElseThrow(() -> new RuntimeException("Blog not found"));

   blog.unpublish();
   return BlogVO.fromEntity(blogRepository.save(blog));
  }
}
