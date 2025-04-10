package me.blog.backend.domain.blog.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.blog.backend.common.exception.BlogNotFoundException;
import me.blog.backend.domain.blog.entity.BlogSeriesEntity;
import me.blog.backend.domain.blog.repository.BlogSeriesRepository;
import me.blog.backend.domain.blog.vo.BlogVO;
import me.blog.backend.domain.blog.entity.BlogEntity;
import me.blog.backend.domain.blog.repository.BlogRepository;

@Service
public class BlogService {
  private final BlogRepository blogRepository;
  private final BlogSeriesRepository blogSeriesRepository;

  public BlogService(BlogRepository blogRepository, BlogSeriesRepository blogSeriesRepository) {
    this.blogRepository = blogRepository;
    this.blogSeriesRepository = blogSeriesRepository;
  }

  @Transactional
  public BlogVO postBlog(String userId, String title, String content, String thumbnailUrl, String type){
    if(type != null && !type.isEmpty()) {
      return handleBlogByType(userId, title, content, thumbnailUrl, type);
    }

    BlogEntity blog = new BlogEntity(userId, title, content, thumbnailUrl);
    return BlogVO.fromEntity(blogRepository.save(blog));
  }

  private BlogVO handleBlogByType(String userId, String title, String content, String thumbnailUrl, String type) {
    Optional<BlogEntity> byType = blogRepository.findByType(type);
    if(byType.isPresent()) {
      return updateBlog(byType.get().getId(), userId, title, content, thumbnailUrl);
    }
    BlogEntity blog = new BlogEntity(userId, title, content, thumbnailUrl, type);
    return BlogVO.fromEntity(blogRepository.save(blog));
  }

  @Transactional
  public BlogVO updateBlog(Long id,String userId, String newTitle, String newContent, String thumbNailUrl) {
    BlogEntity blogEntity = blogRepository.findById(id)
        .orElseThrow(() -> new BlogNotFoundException(String.format("Blog with ID %s not found", id)));

    blogEntity.setAuthor(userId);
    blogEntity.setTitle(newTitle);
    blogEntity.setContent(newContent);
    blogEntity.setUpdatedAt(LocalDateTime.now());
    blogEntity.upLoadThumbnailUrl(thumbNailUrl);

    return BlogVO.fromEntity(blogRepository.save(blogEntity));
  }

  @Transactional
  public BlogVO updateBlogContent(Long id, String newContent) {
    BlogEntity blogEntity = blogRepository.findById(id)
        .orElseThrow(() -> new BlogNotFoundException(String.format("Blog with ID %s not found", id)));

    blogEntity.setContent(newContent);
    return BlogVO.fromEntity(blogRepository.save(blogEntity));
  }

  @Transactional(readOnly = true)
  public List<BlogVO> getAllBlogs() {
    return blogRepository.findAll().stream()
            .filter(BlogEntity::isPublished)
            .map(BlogVO::fromEntity)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<BlogVO> getAllBlogsByUser(String userId) {
    return blogRepository.findByAuthor(userId).stream()
            .map(BlogVO::fromEntity)
            .collect(Collectors.toList());
  }

  @Transactional
  public BlogVO getBlogById(Long id) {
    BlogEntity blogEntity = blogRepository.findById(id)
        .orElseThrow(() -> new BlogNotFoundException(String.format("Blog with ID %s not found", id)));

    blogEntity.readCounting();
    return BlogVO.fromEntity(blogEntity);
  }

  @Transactional(readOnly = true)
  public BlogVO getBlogByType(String type) {
    BlogEntity blog = blogRepository.findByType(type).orElse(
            new BlogEntity("","","""
            {
              "blocks": [
                {
                  "id": "",
                  "type": "paragraph",
                  "data": {
                    "text": ""
                  }
                }
              ],
              "version": ""
            }
            """));

    return BlogVO.fromEntity(blog);
  }

  @Transactional(readOnly = true)
  public Map<String, List<BlogVO>> getBlogGroupBySeries(){
    Map<String, List<BlogVO>> result = new HashMap<>();
    List<BlogSeriesEntity> blogSeriesEntityList = blogSeriesRepository.findAll(Sort.by(Sort.Direction.ASC, "series_id"));
    for (BlogSeriesEntity blogSeriesEntity : blogSeriesEntityList) {
      String key = blogSeriesEntity.getSeries().getName();
      BlogVO blogVO = BlogVO.fromEntity(blogSeriesEntity.getBlog());
      result.putIfAbsent(key, new ArrayList<>());
      result.get(key).add(blogVO);
    }
    return result;
  }

  @Transactional
  public BlogVO publishBlog(Long id){
   BlogEntity blog = blogRepository.findById(id)
       .orElseThrow(() -> new BlogNotFoundException(String.format("Blog with ID %s not found", id)));

   blog.publish();
   return BlogVO.fromEntity(blog);
  }

  @Transactional
  public BlogVO unPublishBlog(Long id){
   BlogEntity blog = blogRepository.findById(id)
       .orElseThrow(() -> new BlogNotFoundException(String.format("Blog with ID %s not found", id)));

   blog.unpublish();
   return BlogVO.fromEntity(blog);
  }
}
