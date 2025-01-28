package me.blog.backend.domain.blog.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.blog.backend.common.exception.BlogNotFoundException;
import me.blog.backend.domain.blog.vo.BlogVO;
import me.blog.backend.domain.blog.entity.BlogEntity;
import me.blog.backend.domain.blog.repository.BlogRepository;

@Service
public class BlogService {
  private final BlogRepository blogRepository;

  public BlogService(BlogRepository blogRepository) {
    this.blogRepository = blogRepository;
  }

  @Transactional
  public BlogVO postBlog(String title, String content, String thumbnailUrl){
    BlogEntity blog = new BlogEntity(title, content, thumbnailUrl);
    return BlogVO.fromEntity(blogRepository.save(blog));
  }

  @Transactional
  public BlogVO updateBlog(Long id, String newTitle, String newContent, String thumbNailUrl) {
    BlogEntity blogEntity = blogRepository.findById(id)
        .orElseThrow(() -> new BlogNotFoundException(String.format("Blog with ID %s not found", id)));

    blogEntity.setTitle(newTitle);
    blogEntity.setContent(newContent);
    blogEntity.setUpdatedAt(LocalDateTime.now());

    if(!thumbNailUrl.equals(blogEntity.getThumbnailUrl()))
      blogEntity.setThumbnailUrl(thumbNailUrl);

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
