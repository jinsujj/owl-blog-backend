package me.blog.backend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import me.blog.backend.adaptors.file.FileStoragePort;
import me.blog.backend.adaptors.file.FileStorageProperties;
import me.blog.backend.adaptors.file.LocalFileStorageAdapter;
import me.blog.backend.adaptors.file.S3FileStorageAdapter;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class FileStorageConfig {
  /*
     determined by application.yml 'spring.profiles.active' value
  */
  @Bean
  @Profile("local")
  public FileStoragePort localFileStorageAdapter(FileStorageProperties fileStorageProperties) {
    return new LocalFileStorageAdapter(fileStorageProperties);
  }

  @Bean
  @Profile("s3")
  public FileStoragePort s3FileStorageAdapter(S3Client s3Client, FileStorageProperties fileStorageProperties) {
    return new S3FileStorageAdapter(s3Client, fileStorageProperties);
  }
}
