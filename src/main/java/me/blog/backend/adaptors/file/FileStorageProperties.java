package me.blog.backend.adaptors.file;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
  private String uploadDir;
  private String baseUrl;
  private String s3Bucket;
  private String s3BaseUrl;
}

