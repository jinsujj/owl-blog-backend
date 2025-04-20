package me.blog.backend.bounded.context.blog.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.model.BlogSeriesEntity;
import me.blog.backend.bounded.context.blog.port.in.SeriesUseCase;
import me.blog.backend.bounded.context.blog.port.out.repository.BlogRepositoryPort;
import me.blog.backend.bounded.context.blog.port.out.repository.BlogSeriesRepositoryPort;
import me.blog.backend.bounded.context.blog.port.out.repository.SeriesRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.blog.backend.bounded.context.blog.domain.model.SeriesEntity;
import me.blog.backend.bounded.context.blog.domain.vo.SeriesVO;

@Service
@RequiredArgsConstructor
public class SeriesService implements SeriesUseCase {
  private final SeriesRepositoryPort seriesRepository;
  private final BlogSeriesRepositoryPort blogSeriesRepository;
  private final BlogRepositoryPort blogRepository;


  @Override
  public List<SeriesVO> getSeries(){
    List<SeriesEntity> seriesList = seriesRepository.findAll();

    List<SeriesVO> seriesVOs = new ArrayList<>();
    for(SeriesEntity series: seriesList){
      seriesVOs.add(SeriesVO.from(series));
    }
    return seriesVOs;
  }


  @Override
  @Transactional
  public Boolean createSeries(String seriesName) {
    Optional<SeriesEntity> series = seriesRepository.findByName(seriesName);
    if(series.isPresent())
      return false;

    seriesRepository.save(new SeriesEntity(seriesName));
    return true;
  }

  @Override
  @Transactional
  public Boolean removeSeries(String seriesName) {
    Optional<SeriesEntity> series = seriesRepository.findByName(seriesName);
    if(!series.isPresent())
      return false;

    seriesRepository.delete(series.get());
    return true;
  }

  @Override
  @Transactional
  public Boolean addBlogToSeries(String blogId, String seriesName) {
    BlogEntity blog = blogRepository.findById(Long.parseLong(blogId))
        .orElseThrow(() -> new IllegalArgumentException("Blog not found with ID: " + blogId));

    SeriesEntity series = seriesRepository.findByName(seriesName)
        .orElseThrow(() -> new IllegalArgumentException("Series not found: " + seriesName));

    blogSeriesRepository.save(new BlogSeriesEntity(blog, series));
    return true;
  }
}
