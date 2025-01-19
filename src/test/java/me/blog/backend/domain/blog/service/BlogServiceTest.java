package me.blog.backend.domain.blog.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.blog.backend.common.exception.BlogNotFoundException;
import me.blog.backend.domain.blog.vo.BlogVO;
import me.blog.backend.domain.blog.entity.BlogEntity;
import me.blog.backend.domain.blog.repository.BlogRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BlogServiceTest {
  @InjectMocks
  private BlogService blogService;
  @Mock
  private BlogRepository blogRepository;

  @BeforeEach
  void setUp(){
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void test_createBlog(){
    // given
    String title = "Test Title";
    String content = "Test Content";
    BlogEntity blogEntity = new BlogEntity(title, content, LocalDateTime.now());
    when(blogRepository.save(any(BlogEntity.class))).thenReturn(blogEntity);

    // when
    BlogVO result = blogService.postBlog(title, content);

    // then
    assertNotNull(result);
    assertEquals(result.title(), title);
    assertEquals(result.content(), content);
    verify(blogRepository, times(1)).save(any(BlogEntity.class));
  };

  @Test
  void test_updateBlog(){
    // given
    Long id = 1L;
    String newTitle = "New Title";
    String newContent = "New Content";
    BlogEntity existingBlog = new BlogEntity("Old Title", "Old Content", LocalDateTime.now());
    when(blogRepository.findById(id)).thenReturn(Optional.of(existingBlog));
    when(blogRepository.save(existingBlog)).thenReturn(existingBlog);

    // when
    BlogVO result = blogService.updateBlog(id, newTitle, newContent);

    // then
    assertNotNull(result);
    assertEquals(result.title(), newTitle);
    assertEquals(result.content(), newContent);
    verify(blogRepository, times(1)).findById(id);
    verify(blogRepository, times(1)).save(any(BlogEntity.class));
  }

  @Test
  void test_getAllBlogs(){
    // given
    BlogEntity blog1 = new BlogEntity("Title 1", "Content 1", LocalDateTime.now());
    BlogEntity blog2 = new BlogEntity("Title 2", "Content 2", LocalDateTime.now());
    when(blogRepository.findAll()).thenReturn(Arrays.asList(blog1, blog2));

    // when
    List<BlogVO> result = blogService.getAllBlogs();

    // then
    assertNotNull(result);
    assertEquals(result.size(), 2);
    verify(blogRepository, times(1)).findAll();
  }

  @Test
  void test_publishBlog() {
    // given
    Long id = 1L;
    BlogEntity blogEntity = new BlogEntity("Title", "Content", LocalDateTime.now());
    when(blogRepository.findById(id)).thenReturn(Optional.of(blogEntity));
    when(blogRepository.save(blogEntity)).thenReturn(blogEntity);

    // when
    BlogVO result = blogService.publishBlog(id);

    // then
    assertNotNull(result);
    assertTrue(blogEntity.isPublished());
    verify(blogRepository, times(1)).findById(id);
    verify(blogRepository, times(1)).save(blogEntity);
  }

  @Test
  void test_getBlogById_with_non_published_blog(){
    // given
    Long id =1L;
    String title = "Test Title";
    String content = "Test Content";
    BlogEntity blog = new BlogEntity(title, content, LocalDateTime.now());
    when(blogRepository.findById(id)).thenReturn(Optional.of(blog));

    // when && then
    BlogNotFoundException exception = assertThrows(BlogNotFoundException.class, () -> blogService.getBlogById(id));
    assertEquals("Blog with ID 1 not found", exception.getMessage());
    verify(blogRepository, times(1)).findById(id);
  }

  @Test
  void test_getBlogById_with_published_blog(){
    // given
    Long id =1L;
    String title = "Test Title";
    String content = "Test Content";
    int readCount =11;
    BlogEntity blog = new BlogEntity(title, content, readCount,LocalDateTime.now(), null, null);
    blog.publish();
    when(blogRepository.findById(id)).thenReturn(Optional.of(blog));

    // when
    BlogVO result = blogService.getBlogById(id);

    // then
    assertNotNull(result);
    assertEquals(result.readCount(), readCount+1);
    assertEquals(result.title(), blog.getTitle());
    assertEquals(result.content(), blog.getContent());
    verify(blogRepository, times(1)).findById(id);
  }

}