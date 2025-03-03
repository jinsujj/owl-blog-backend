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
    String author = "12";
    String title = "Test Title";
    String content = "Test Content";
    String thumbnailUrl ="https://backend.owl-dev.me/files/substation2.0.png";
    BlogEntity blogEntity = new BlogEntity(title, content, thumbnailUrl);
    when(blogRepository.save(any(BlogEntity.class))).thenReturn(blogEntity);

    // when
    BlogVO result = blogService.postBlog(author,title, content, thumbnailUrl,"");

    // then
    assertNotNull(result);
    assertEquals(result.author(), author);
    assertEquals(result.title(), title);
    assertEquals(result.content(), content);
    assertEquals(result.thumbnailUrl(), thumbnailUrl);
    verify(blogRepository, times(1)).save(any(BlogEntity.class));
  };

  @Test
  void test_updateBlog(){
    // given
    Long id = 1L;
    String author = "12";
    String newTitle = "New Title";
    String newContent = "New Content";
    String thumbnailUrl ="https://backend.owl-dev.me/files/substation2.0.png";
    BlogEntity existingBlog = new BlogEntity("Old Title", "Old Content", thumbnailUrl);
    when(blogRepository.findById(id)).thenReturn(Optional.of(existingBlog));
    when(blogRepository.save(existingBlog)).thenReturn(existingBlog);

    // when
    BlogVO result = blogService.updateBlog(id,author, newTitle, newContent, thumbnailUrl);

    // then
    assertNotNull(result);
    assertEquals(result.title(), newTitle);
    assertEquals(result.content(), newContent);
    assertEquals(result.thumbnailUrl(), thumbnailUrl);
    verify(blogRepository, times(1)).findById(id);
    verify(blogRepository, times(1)).save(any(BlogEntity.class));
  }

  @Test
  void test_getAllBlogs(){
    // given
    BlogEntity blog1 = new BlogEntity("1","Title 1", "Content 1");
    BlogEntity blog2 = new BlogEntity("1","Title 2", "Content 2");
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
    BlogEntity blogEntity = new BlogEntity("13","Title", "Content");
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
    String author = "1";
    String title = "Test Title";
    String content = "Test Content";
    BlogEntity blog = new BlogEntity(author, title, content);
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
    String author = "1";
    String title = "Test Title";
    String content = "Test Content";
    int readCount =11;
    BlogEntity blog = new BlogEntity(author, title, content);
    blog.setReadCount(readCount);
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