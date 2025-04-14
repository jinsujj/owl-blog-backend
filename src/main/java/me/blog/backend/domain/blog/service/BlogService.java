package me.blog.backend.domain.blog.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import me.blog.backend.domain.blog.cache.BlogCache;
import me.blog.backend.domain.blog.cache.BlogSeriesCache;
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
  private final BlogCache blogCache;
  private final BlogSeriesCache blogSeriesCache;


  public BlogService(BlogRepository blogRepository, BlogSeriesRepository blogSeriesRepository, BlogCache blogCache, BlogSeriesCache blogSeriesCache) {
    this.blogRepository = blogRepository;
    this.blogSeriesRepository = blogSeriesRepository;
    this.blogCache = blogCache;
    this.blogSeriesCache = blogSeriesCache;
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
    BlogEntity blog = blogCache.findByType(type)
      .orElseGet(() -> blogRepository.findByType(type)
        .orElseThrow(() -> new BlogNotFoundException(type))
    );

    return updateBlog(blog.getId(), userId, title, content, thumbnailUrl);
  }

  @Transactional
  public BlogVO updateBlog(Long id,String userId, String newTitle, String newContent, String thumbNailUrl) {
    BlogEntity blogEntity = blogRepository.findById(id)
      .orElseThrow(() -> new BlogNotFoundException("Blog with ID %s not found".formatted(id))
    );

    blogEntity.setAuthor(userId);
    blogEntity.setTitle(newTitle);
    blogEntity.setContent(newContent);
    blogEntity.setUpdatedAt(LocalDateTime.now());
    blogEntity.upLoadThumbnailUrl(thumbNailUrl);

    BlogEntity savedEntity = blogRepository.save(blogEntity);
    blogCache.putAll();

    return BlogVO.fromEntity(savedEntity);
  }

  @Transactional
  public BlogVO updateBlogContent(Long id, String newContent) {
    BlogEntity blogEntity = blogRepository.findById(id)
      .orElseThrow(() -> new BlogNotFoundException("Blog with ID %s not found".formatted(id))
    );

    blogEntity.setContent(newContent);
    BlogEntity savedEntity = blogRepository.save(blogEntity);
    blogCache.putAll();

    return BlogVO.fromEntity(savedEntity);
  }

  @Transactional(readOnly = true)
  public List<BlogVO> getAllBlogs() {
    List<BlogEntity> cachedBlogs = blogCache.findAll();
    List<BlogEntity> blogs = cachedBlogs.isEmpty()
            ? blogRepository.findAll()
            : cachedBlogs;

    return blogs.stream()
            .filter(BlogEntity::isPublished)
            .map(BlogVO::fromEntity)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<BlogVO> getAllBlogsByUser(String userId) {
    List<BlogEntity> cachedBlogs = blogCache.findByAuthor(userId);
    List<BlogEntity> blogs = cachedBlogs.isEmpty()
            ? blogRepository.findByAuthor(userId)
            : cachedBlogs;

    return blogs.stream()
            .map(BlogVO::fromEntity)
            .collect(Collectors.toList());
  }

  @Transactional
  public BlogVO getBlogById(Long id) {
    BlogEntity blogEntity = blogCache.findById(id)
      .orElseGet(() -> blogRepository.findById(id)
        .orElseThrow(() -> new BlogNotFoundException("Blog with ID %s not found".formatted(id)))
      );

    blogRepository.incrementReadCount(id);
    return BlogVO.fromEntity(blogEntity);
  }

  @Transactional(readOnly = true)
  public BlogVO getBlogByType(String type) {
    BlogEntity blog= blogCache.findByType(type).orElseGet(
      () -> blogRepository.findByType(type)
        .orElse(new BlogEntity("","","""
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
          """
        ))
    );

    return BlogVO.fromEntity(blog);
  }

  @Transactional(readOnly = true)
  public Map<String, List<BlogVO>> getBlogGroupBySeries(){
    Map<String, List<BlogVO>> result = new HashMap<>();
    List<BlogSeriesEntity> series = blogSeriesCache.findAll();
    List<BlogSeriesEntity> blogSeriesEntityList = series.isEmpty()
            ? blogSeriesRepository.findAll()
            : series;

    blogSeriesEntityList = blogSeriesEntityList.stream()
            .sorted(Comparator.comparing(BlogSeriesEntity::getId)).toList();

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
   blogCache.putAll();
   return BlogVO.fromEntity(blog);
  }

  @Transactional
  public BlogVO unPublishBlog(Long id){
   BlogEntity blog = blogRepository.findById(id)
       .orElseThrow(() -> new BlogNotFoundException(String.format("Blog with ID %s not found", id)));

   blog.unpublish();
   blogCache.putAll();
   return BlogVO.fromEntity(blog);
  }
}
