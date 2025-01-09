package me.blog.backend.domain.blog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.blog.backend.common.exception.BlogNotFoundException;

@Service
public class BlogService {
  private final BlogRepository blogRepository;

  public BlogService(BlogRepository blogRepository) {
    this.blogRepository = blogRepository;
  }

  @Transactional
  public BlogVO createBlog(String title, String content){
    BlogEntity blog = new BlogEntity(title, content, LocalDateTime.now());
    return BlogVO.fromEntity(blogRepository.save(blog));
  }

  @Transactional
  public BlogVO updateBlog(Long id, String newTitle, String newContent) {
    BlogEntity blogEntity = blogRepository.findById(id)
        .orElseThrow(() -> new BlogNotFoundException(String.format("Blog with ID %s not found", id)));
    blogEntity.setTitle(newTitle);
    blogEntity.setContent(newContent);
    blogEntity.setUpdatedAt(LocalDateTime.now());
    return BlogVO.fromEntity(blogRepository.save(blogEntity));
  }

  @Transactional(readOnly = true)
  public List<BlogVO> getAllBlogs() {
    return blogRepository.findAll().stream()
        .map(BlogVO::fromEntity).collect(Collectors.toList());
  }

  @Transactional
  public BlogVO getBlogById(Long id) {
    BlogEntity blogEntity = blogRepository.findById(id)
        .filter(BlogEntity::isPublished)
        .orElseThrow(() -> new BlogNotFoundException(String.format("Blog with ID %s not found", id)));

    blogEntity.readCounting();
    return BlogVO.fromEntity(blogEntity);
  }

  @Transactional
  public BlogVO publishBlog(Long id){
   BlogEntity blog = blogRepository.findById(id)
       .orElseThrow(() -> new BlogNotFoundException(String.format("Blog with ID %s not found", id)));

   blog.publish();
   return BlogVO.fromEntity(blogRepository.save(blog));
  }

  @Transactional
  public BlogVO unPublishBlog(Long id){
   BlogEntity blog = blogRepository.findById(id)
       .orElseThrow(() -> new BlogNotFoundException(String.format("Blog with ID %s not found", id)));

   blog.unpublish();
   return BlogVO.fromEntity(blogRepository.save(blog));
  }
}
