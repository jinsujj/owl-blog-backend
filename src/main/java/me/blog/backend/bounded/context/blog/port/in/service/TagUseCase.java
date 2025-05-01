package me.blog.backend.bounded.context.blog.port.in.service;

import me.blog.backend.bounded.context.blog.domain.vo.TagVO;

import java.util.List;

public interface TagUseCase {
    void postTags(List<TagVO> tags, Long blogId);
    void updateTags(List<TagVO> tags, Long blogId);
    TagVO[] getTagsAll();
    TagVO[] getTagByBlogId(Long blogId);
}
