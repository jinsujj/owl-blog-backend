package me.blog.backend.bounded.context.blog.application.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.model.BlogSeriesEntity;
import me.blog.backend.bounded.context.blog.port.in.BlogUseCase;
import me.blog.backend.bounded.context.blog.port.out.cache.BlogCachePort;
import me.blog.backend.bounded.context.blog.port.out.repository.BlogRepositoryPort;
import me.blog.backend.bounded.context.blog.port.out.cache.BlogSeriesCachePort;
import me.blog.backend.bounded.context.blog.port.out.repository.BlogSeriesRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.blog.backend.common.exception.BlogNotFoundException;
import me.blog.backend.bounded.context.blog.domain.vo.BlogVO;

@Service
@RequiredArgsConstructor
public class BlogService implements BlogUseCase {
  private final BlogRepositoryPort blogRepository;
  private final BlogSeriesRepositoryPort blogSeriesRepository;
  private final BlogCachePort blogCache;
  private final BlogSeriesCachePort blogSeriesCache;
  private final AiService aiService;

  @Override
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

  @Override
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

  @Override
  @Transactional
  public BlogVO updateBlogContent(Long id, String newContent) {
    BlogEntity blogEntity = blogRepository.findById(id)
      .orElseThrow(() -> new BlogNotFoundException("Blog with ID %s not found".formatted(id))
    );

    blogEntity.setContent(newContent);
    BlogEntity savedEntity = blogRepository.save(blogEntity);
    blogCache.putAll();
    aiService.publishSummary(id);

    return BlogVO.fromEntity(savedEntity);
  }

  @Override
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

  @Override
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

  @Override
  @Transactional
  public BlogVO getBlogById(Long id) {
    BlogEntity blogEntity = blogCache.findById(id)
      .orElseGet(() -> blogRepository.findById(id)
        .orElseThrow(() -> new BlogNotFoundException("Blog with ID %s not found".formatted(id)))
      );

    blogRepository.incrementReadCount(id);
    return BlogVO.fromEntity(blogEntity);
  }

  @Override
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

  @Override
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

  @Override
  @Transactional
  public BlogVO publishBlog(Long id){
   BlogEntity blog = blogRepository.findById(id)
       .orElseThrow(() -> new BlogNotFoundException(String.format("Blog with ID %s not found", id)));

   blog.publish();
   blogCache.putAll();
   aiService.publishSummary(id);

   return BlogVO.fromEntity(blog);
  }

  @Override
  @Transactional
  public BlogVO unPublishBlog(Long id){
   BlogEntity blog = blogRepository.findById(id)
       .orElseThrow(() -> new BlogNotFoundException(String.format("Blog with ID %s not found", id)));

   blog.unpublish();
   blogCache.putAll();
   return BlogVO.fromEntity(blog);
  }
}
