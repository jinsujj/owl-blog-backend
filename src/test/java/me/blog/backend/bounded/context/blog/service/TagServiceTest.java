package me.blog.backend.bounded.context.blog.service;

import me.blog.backend.bounded.context.blog.application.service.TagService;
import me.blog.backend.bounded.context.blog.adapter.out.database.BlogTagRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.junit.jupiter.api.BeforeEach;
import me.blog.backend.bounded.context.blog.adapter.out.database.BlogRepository;
import me.blog.backend.bounded.context.blog.adapter.out.database.TagRepository;

class TagServiceTest {
  @Mock
  private TagRepository tagRepository;
  @Mock
  private BlogRepository blogRepository;
  @Mock
  private BlogTagRepository blogTagRepository;
  @InjectMocks
  private TagService tagService;

  @BeforeEach
  void setUp(){
    MockitoAnnotations.openMocks(this);
  }
}