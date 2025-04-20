package me.blog.backend.bounded.context.blog.adapter.out.file;

import java.io.IOException;

import me.blog.backend.bounded.context.blog.port.out.FileStoragePort;
import org.springframework.web.multipart.MultipartFile;

import me.blog.backend.common.exception.FileStorageException;
import me.blog.backend.bounded.context.blog.domain.vo.FileUploadResponse;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;


public class S3FileStorageAdapter implements FileStoragePort {
  private final FileStorageProperties fileStorageProperties;
  private final S3Client s3Client;

  public S3FileStorageAdapter(S3Client s3Client, FileStorageProperties fileStorageProperties) {
      this.s3Client = s3Client;
      this.fileStorageProperties = fileStorageProperties;
  }

  @Override
  public FileUploadResponse saveFile(MultipartFile file) {
    try{
      String bucketName = fileStorageProperties.getS3Bucket();
      String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

      s3Client.putObject(
          PutObjectRequest.builder().bucket(bucketName)
              .key(fileName)
              .contentType(file.getContentType())
              .build(),
          RequestBody.fromBytes(file.getBytes())
      );

      String fileUrl = fileStorageProperties.getS3BaseUrl() + "/" + fileName;
      return new FileUploadResponse(fileUrl);
    }
    catch (IOException e){
      throw new FileStorageException("failed when file upload "+e);
    }
    catch (S3Exception e){
      throw new FileStorageException("failed when upload file to S3 "+e);
    }
  }

  @Override
  public void deleteFile(String fileName) {
    try{
      String bucketName = fileStorageProperties.getS3Bucket();

      s3Client.deleteBucket(DeleteBucketRequest.builder()
          .bucket(bucketName)
          .bucket(fileName)
          .build());
    }
    catch (S3Exception e){
      throw new FileStorageException("failed when delete file in s3 "+e);
    }
  }
}
