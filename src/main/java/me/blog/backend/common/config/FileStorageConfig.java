package me.blog.backend.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.blog.backend.bounded.context.blog.port.out.file.FileStoragePort;
import me.blog.backend.bounded.context.blog.adapter.out.file.FileStorageProperties;
import me.blog.backend.bounded.context.blog.adapter.out.file.LocalFileStorageAdapter;
import me.blog.backend.bounded.context.blog.adapter.out.file.S3FileStorageAdapter;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class FileStorageConfig {
  /*
     determined by application.yml 'file.storage.type' value
  */
  @Bean
  @ConditionalOnProperty(name = "file.storage.type", havingValue = "local")
  public FileStoragePort localFileStorageAdapter(FileStorageProperties fileStorageProperties) {
    return new LocalFileStorageAdapter(fileStorageProperties);
  }

  @Bean
  @ConditionalOnProperty(name = "file.storage.type", havingValue = "s3")
  public FileStoragePort s3FileStorageAdapter(S3Client s3Client, FileStorageProperties fileStorageProperties) {
    return new S3FileStorageAdapter(s3Client, fileStorageProperties);
  }
}
