package me.blog.backend.bounded.context.blog.application.service;

import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.port.in.service.TagUseCase;
import me.blog.backend.bounded.context.blog.port.out.repository.BlogRepositoryPort;
import me.blog.backend.bounded.context.blog.port.out.repository.BlogTagRepositoryPort;
import me.blog.backend.bounded.context.blog.port.out.repository.TagRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.blog.backend.common.exception.BlogNotFoundException;
import me.blog.backend.bounded.context.blog.domain.vo.TagVO;
import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.model.BlogTagEntity;
import me.blog.backend.bounded.context.blog.domain.model.TagEntity;

@Service
@RequiredArgsConstructor
public class TagService implements TagUseCase {
  private final TagRepositoryPort tagRepository;
  private final BlogRepositoryPort blogRepository;
  private final BlogTagRepositoryPort blogTagRepository;

  @Override
  @Transactional
  public void postTags(List<TagVO> tags, Long blogId) {
    var blog = getBlogEntity(blogId);

    for (TagVO tag : tags) {
        TagEntity newTag = tagRepository.findByValue(tag.name()).stream()
                .findFirst()
                .orElseGet(() -> tagRepository.save(new TagEntity(tag.name(), tag.label())));
        blog.addTag(newTag);
    }

    blogRepository.save(blog);
  }

  @Override
  @Transactional
  public void updateTags(List<TagVO> tags, Long blogId) {
    var blog = getBlogEntity(blogId);
    List<BlogTagEntity> blogTagList = blogTagRepository.findByBlog(blog);
    blog.getBlogTags().clear();

    for (TagVO tag : tags) {
      TagEntity newTag = tagRepository.findByValue(tag.name()).stream()
              .findFirst()
              .orElseGet(() -> tagRepository.save(new TagEntity(tag.name(), tag.label())));
      blog.addTag(newTag);
    }

    for(BlogTagEntity tag : blogTagList) {
      List<BlogTagEntity> byTag = blogTagRepository.findByTag(tag.getTag());
      if(byTag.isEmpty()) {
        tagRepository.delete(tag.getTag());
      }
    }
  }

  @Override
  @Transactional(readOnly = true)
  public TagVO[] getTagsAll(){
    return TagVO.from(tagRepository.findAll());
  }

  @Override
  public TagVO[] getTagByBlogId(Long blogId){
    var blog = getBlogEntity(blogId);

    Set<BlogTagEntity> blogTags = blog.getBlogTags();
    return TagVO.from(blogTags);
  }

  private BlogEntity getBlogEntity(Long blogId) {
    return blogRepository.findById(blogId)
        .orElseThrow(() -> new BlogNotFoundException(String.format("Blog with ID %s not found", blogId)));
  }
}
