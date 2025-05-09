package me.blog.backend.bounded.context.blog.domain.vo;

import java.util.List;
import java.util.Set;

import me.blog.backend.bounded.context.blog.domain.model.TagEntity;
import me.blog.backend.bounded.context.blog.domain.model.BlogTagEntity;

public record TagVO(String name, String label){
  public static TagVO from(TagEntity tag) {
    return new TagVO(tag.getValue(), tag.getLabel());
  }

  public static TagVO[] from(List<TagEntity> tags) {
    TagVO[] result = new TagVO[tags.size()];
    for(int i = 0; i < tags.size(); i++) {
      result[i] = from(tags.get(i));
    }
    return result;
  }

  public static TagVO[] from(Set<BlogTagEntity>tags){
    TagVO[] result = new TagVO[tags.size()];
    int index = 0;
    for(BlogTagEntity tag : tags) {
      result[index++] = from(tag.getTag());
    }
    return result;
  }
}