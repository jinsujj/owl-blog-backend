package me.blog.backend.domain.blog.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.hibernate.internal.SessionImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.blog.backend.domain.blog.entity.BlogEntity;
import me.blog.backend.domain.blog.entity.BlogSeriesEntity;
import me.blog.backend.domain.blog.entity.SeriesEntity;
import me.blog.backend.domain.blog.repository.BlogRepository;
import me.blog.backend.domain.blog.repository.BlogSeriesRepository;
import me.blog.backend.domain.blog.repository.SeriesRepository;
import me.blog.backend.domain.blog.vo.SeriesVO;

@Service
public class SeriesService {
  @PersistenceContext
  private EntityManager entityManager;
  private final SeriesRepository seriesRepository;
  private final BlogSeriesRepository blogSeriesRepository;
  private final BlogRepository blogRepository;

  public SeriesService(SeriesRepository seriesRepository, BlogSeriesRepository blogSeriesRepository, BlogRepository blogRepository) {
    this.seriesRepository = seriesRepository;
    this.blogSeriesRepository = blogSeriesRepository;
    this.blogRepository = blogRepository;
  }

  public List<SeriesVO> getSeries(){
    List<SeriesEntity> seriesList = seriesRepository.findAll();

    List<SeriesVO> seriesVOs = new ArrayList<>();
    for(SeriesEntity series: seriesList){
      seriesVOs.add(SeriesVO.from(series));
    }
    return seriesVOs;
  }


  @Transactional
  public Boolean createSeries(String seriesName) {
    Optional<SeriesEntity> series = seriesRepository.findByName(seriesName);
    if(series.isPresent())
      return false;

    seriesRepository.save(new SeriesEntity(seriesName));
    return true;
  }

  @Transactional
  public Boolean removeSeries(String seriesName) {
    Optional<SeriesEntity> series = seriesRepository.findByName(seriesName);
    if(!series.isPresent())
      return false;

    seriesRepository.delete(series.get());
    return true;
  }

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
