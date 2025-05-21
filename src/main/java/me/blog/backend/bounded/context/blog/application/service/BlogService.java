package me.blog.backend.bounded.context.blog.application.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.model.BlogSeriesEntity;
import me.blog.backend.bounded.context.blog.domain.vo.BlogSeriesVO;
import me.blog.backend.bounded.context.blog.port.in.service.BlogUseCase;
import me.blog.backend.bounded.context.blog.port.in.service.SummaryUseCase;
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
  private final SummaryUseCase aiService;

  @Override
  @Transactional
  public BlogVO postBlog(String userId, String title, String content, String thumbnailUrl, String type){
    // post with type
    if(type != null && !type.isEmpty())
      return postBlogWithType(userId, title, content, thumbnailUrl, type);

    // post without type
    BlogEntity blog = new BlogEntity(userId, title, content, thumbnailUrl);
    blogCache.putAll();
    
    return BlogVO.fromEntity(blogRepository.save(blog));
  }

  private BlogVO postBlogWithType(String userId, String title, String content, String thumbnailUrl, String type) {
    return blogCache.findByType(type)
        .map(BlogVO::id)  // blogId from cache
        .or(() -> blogRepository.findByType(type).map(BlogEntity::getId)) // blogId from db
        .map(id -> updateBlog(id, userId, title, content, thumbnailUrl))
        .orElseThrow(() -> new BlogNotFoundException(type));
  }


  @Override
  @Transactional
  public BlogVO updateBlog(Long id,String userId, String newTitle, String newContent, String thumbNailUrl) {
    BlogEntity blogEntity = blogRepository.findById(id)
      .orElseThrow(() -> new BlogNotFoundException("Blog with ID %s not found".formatted(id)));

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
      .orElseThrow(() -> new BlogNotFoundException("Blog with ID %s not found".formatted(id)));

    blogEntity.setContent(newContent);
    BlogEntity savedEntity = blogRepository.save(blogEntity);
    blogCache.putAll();
    aiService.publishSummary(id);

    return BlogVO.fromEntity(savedEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public List<BlogVO> getAllBlogs() {
    List<BlogVO> cachedBlogs = blogCache.findAll();
    return cachedBlogs.isEmpty()
        // from db
        ? blogRepository.findAll().stream()
        .filter(BlogEntity::isPublished)
        .map(BlogVO::fromEntity)
        .toList()
        // from cache
        : cachedBlogs.stream()
        .filter(blog -> blog.publishedAt() != null)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<BlogVO> getAllBlogsByUser(String userId) {
    List<BlogVO> cachedBlogs = blogCache.findByAuthor(userId);
    return cachedBlogs.isEmpty()
        // from db
        ? blogRepository.findByAuthor(userId).stream()
        .map(BlogVO::fromEntity)
        .collect(Collectors.toList())
        // from cache
        : cachedBlogs;
  }

  @Override
  @Transactional
  public BlogVO getBlogById(Long id) {
    return blogCache.findById(id)
        .orElseGet(() -> BlogVO.fromEntity(blogRepository.findById(id)
        .orElseThrow(() -> new BlogNotFoundException("Blog with ID %s not found".formatted(id)))));
  }

  @Override
  @Transactional(readOnly = true)
  public BlogVO getBlogByType(String type) {
    //from cache
    return blogCache.findByType(type).orElseGet(() ->
        // from db
        BlogVO.fromEntity(blogRepository.findByType(type).orElse((
          // default data
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
          """))))
    );
  }

  @Override
  @Transactional(readOnly = true)
  public Map<String, List<BlogVO>> getBlogGroupBySeries(){
    Map<String, List<BlogVO>> result = new HashMap<>();
    List<BlogSeriesVO> cachedSeries = blogSeriesCache.findAll().stream()
        .sorted(Comparator.comparing(BlogSeriesVO::id)).toList();

    if(cachedSeries.isEmpty()){
      List<BlogSeriesEntity> list = blogSeriesRepository.findAll();
      for(BlogSeriesEntity blog : list){
        cachedSeries.add(BlogSeriesVO.fromEntity(blog));
      }
    }
    return cachedSeries.stream()
        .collect(Collectors.groupingBy(BlogSeriesVO::seriesName,
            Collectors.mapping(BlogSeriesVO::blog, Collectors.toList())));
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
