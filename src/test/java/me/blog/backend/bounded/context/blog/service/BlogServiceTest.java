package me.blog.backend.bounded.context.blog.service;


import me.blog.backend.bounded.context.blog.application.service.BlogService;
import me.blog.backend.bounded.context.blog.adapter.out.database.BlogRepository;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.junit.jupiter.api.BeforeEach;


class BlogServiceTest {
  @InjectMocks
  private BlogService blogService;
  @Mock
  private BlogRepository blogRepository;

  @BeforeEach
  void setUp(){
    MockitoAnnotations.openMocks(this);
  }

}