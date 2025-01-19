package me.blog.backend.domain.blog.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.blog.backend.common.exception.BlogNotFoundException;
import me.blog.backend.domain.blog.entity.BlogEntity;
import me.blog.backend.domain.blog.entity.BlogTagEntity;
import me.blog.backend.domain.blog.entity.TagEntity;
import me.blog.backend.domain.blog.repository.BlogRepository;
import me.blog.backend.domain.blog.repository.TagRepository;
import me.blog.backend.domain.blog.vo.TagVO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TagServiceTest {
  @Mock
  private TagRepository tagRepository;
  @Mock
  private BlogRepository blogRepository;
  @InjectMocks
  private TagService tagService;

  @BeforeEach
  void setUp(){
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void test_postTags(){
    // given
    Long blogId = 1L;
    BlogEntity mockBlog = mock(BlogEntity.class);
    TagEntity mockTag = new TagEntity("tag1", "label1");
    List<TagVO> tags = List.of(new TagVO("tag1", "label1"));
    when(blogRepository.findById(blogId)).thenReturn(Optional.of(mockBlog));
    when(tagRepository.findByName(anyString())).thenReturn(List.of());
    when(tagRepository.save(any(TagEntity.class))).thenReturn(mockTag);

    // when
    tagService.postTags(tags, blogId);

    // then
    verify(blogRepository).findById(blogId);
    verify(tagRepository, times(1)).findByName(anyString());
    verify(tagRepository, times(1)).save(any(TagEntity.class));
    verify(mockBlog, times(1)).addTag(any(TagEntity.class));
    verify(blogRepository).save(mockBlog);
  }

  @Test
  void test_postTags_use_existing_tag_if_tag_exists(){
    // given
    Long blogId = 1L;
    BlogEntity mockBlog = mock(BlogEntity.class);
    TagEntity existingTag = new TagEntity("tag1", "label1");
    List<TagVO> tags = List.of(new TagVO("tag1", "label1"));

    when(blogRepository.findById(blogId)).thenReturn(Optional.of(mockBlog));
    when(tagRepository.findByName(anyString())).thenReturn(List.of(existingTag));

    // when
    tagService.postTags(tags, blogId);

    // then
    verify(tagRepository, never()).save(any(TagEntity.class));
    verify(mockBlog).addTag(existingTag);
    verify(blogRepository).save(mockBlog);
  }

  @Test
  void test_getTagsAll(){
    // given
    List<TagEntity> tagEntities = List.of(
        new TagEntity("tag1", "label1"),
        new TagEntity("tag2", "label2")
    );
    when(tagRepository.findAll()).thenReturn(tagEntities);

    // when
    TagVO[] result = tagService.getTagsAll();

    // then
    assertThat(result).hasSize(2);
    assertThat(result).extracting(TagVO::name).containsExactly("tag1","tag2");
    verify(tagRepository).findAll();
  }

  @Test
  void test_getTagByBlogId(){
    // given
    Long blogId = 1L;
    BlogEntity mockBlog = mock(BlogEntity.class);
    BlogTagEntity blogTag1 = new BlogTagEntity(mockBlog, new TagEntity("tag1","label1"));
    BlogTagEntity blogTag2 = new BlogTagEntity(mockBlog, new TagEntity("tag2","label2"));

    when(blogRepository.findById(blogId)).thenReturn(Optional.of(mockBlog));
    when(mockBlog.getBlogTags()).thenReturn(Set.of(blogTag1,blogTag2));

    // when
    TagVO[] result = tagService.getTagByBlogId(blogId);

    // then
    assertThat(result).hasSize(2);
    assertThat(result).extracting(TagVO::name).containsExactly("tag1","tag2");
    verify(blogRepository).findById(blogId);
    verify(mockBlog).getBlogTags();
  }

  @Test
  void test_getTagByBlogId_throw_exception_if_blog_not_exists(){
    // given
    Long blogId =1L;
    when(blogRepository.findById(blogId)).thenReturn(Optional.empty());

    // when && then
    assertThatThrownBy(() ->tagService.getTagByBlogId(blogId))
        .isInstanceOf(BlogNotFoundException.class)
        .hasMessageContaining("Blog with ID 1 not found");

    verify(blogRepository).findById(blogId);
  }
}