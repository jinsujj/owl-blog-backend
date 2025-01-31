package me.blog.backend.domain.blog.service;

import java.util.List;
import java.util.Set;

import me.blog.backend.domain.blog.repository.BlogTagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.blog.backend.common.exception.BlogNotFoundException;
import me.blog.backend.domain.blog.vo.TagVO;
import me.blog.backend.domain.blog.entity.BlogEntity;
import me.blog.backend.domain.blog.entity.BlogTagEntity;
import me.blog.backend.domain.blog.entity.TagEntity;
import me.blog.backend.domain.blog.repository.BlogRepository;
import me.blog.backend.domain.blog.repository.TagRepository;

@Service
public class TagService {
  private final TagRepository tagRepository;
  private final BlogRepository blogRepository;
  private final BlogTagRepository blogTagRepository;

  public TagService(TagRepository tagRepository, BlogRepository blogRepository, BlogTagRepository blogTagRepository) {
    this.tagRepository = tagRepository;
    this.blogRepository = blogRepository;
    this.blogTagRepository = blogTagRepository;
  }

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

  @Transactional
  public void updateTags(List<TagVO> tags, Long blogId) {
    var blog = getBlogEntity(blogId);
    List<BlogTagEntity> blogTagList = blogTagRepository.findByBlog(blog);

    blog.getBlogTags().clear();
    blogTagRepository.deleteAll(blogTagList);
    blogTagRepository.flush();

    for (TagVO tag : tags) {
      TagEntity newTag = tagRepository.findByValue(tag.name()).stream()
              .findFirst()
              .orElseGet(() -> tagRepository.save(new TagEntity(tag.name(), tag.label())));
      blog.addTag(newTag);
    }

    for(BlogTagEntity tag : blogTagList) {
      List<BlogTagEntity> byTag = blogTagRepository.findByTag(tag.getTag());
      if(byTag.isEmpty()) {
        blogTagRepository.delete(tag);
      }
    }
  }

  @Transactional(readOnly = true)
  public TagVO[] getTagsAll(){
    return TagVO.from(tagRepository.findAll());
  }

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
