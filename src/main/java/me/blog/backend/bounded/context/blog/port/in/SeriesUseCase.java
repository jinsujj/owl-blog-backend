package me.blog.backend.bounded.context.blog.port.in;

import me.blog.backend.bounded.context.blog.domain.vo.SeriesVO;

import java.util.List;

public interface SeriesUseCase {
    List<SeriesVO> getSeries();
    Boolean createSeries(String seriesName);
    Boolean removeSeries(String seriesName);
    Boolean addBlogToSeries(String blogId, String seriesName);
}
