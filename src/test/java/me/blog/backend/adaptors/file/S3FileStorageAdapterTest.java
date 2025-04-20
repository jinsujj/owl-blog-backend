package me.blog.backend.adaptors.file;

import java.io.IOException;

import me.blog.backend.bounded.context.blog.adapter.out.file.FileStorageProperties;
import me.blog.backend.bounded.context.blog.adapter.out.file.S3FileStorageAdapter;
import org.springframework.web.multipart.MultipartFile;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.blog.backend.common.exception.FileStorageException;
import me.blog.backend.bounded.context.blog.domain.vo.FileUploadResponse;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

class S3FileStorageAdapterTest {
  @Mock
  private S3Client s3Client;

  @Mock
  private FileStorageProperties fileStorageProperties;

  @InjectMocks
  private S3FileStorageAdapter s3FileStorageAdapter;

  @BeforeEach
  void setUp(){
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void test_saveFile() throws IOException {
    // given 
    MultipartFile file = mock(MultipartFile.class);
    when(file.getOriginalFilename()).thenReturn("test.txt");
    when(file.getContentType()).thenReturn("text/plain");
    when(file.getBytes()).thenReturn("test content".getBytes());
    when(fileStorageProperties.getS3Bucket()).thenReturn("test-bucket");
    when(fileStorageProperties.getS3BaseUrl()).thenReturn("https://s3.amazonaws.com/test-bucket");

    // when 
    FileUploadResponse response = s3FileStorageAdapter.saveFile(file);

    // then
    assertNotNull(response);
    assertTrue(response.getFileUrl().startsWith("https://s3.amazonaws.com/test-bucket"));
    verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
  }

  @Test
  void test_saveFile_when_IOException() throws IOException {
    // given
    MultipartFile file = mock(MultipartFile.class);
    when(file.getOriginalFilename()).thenReturn("test.txt");
    when(file.getBytes()).thenThrow(new IOException("Test IOException"));

    // when && then
    FileStorageException exception = assertThrows(FileStorageException.class, () -> s3FileStorageAdapter.saveFile(file));
    assertTrue(exception.getMessage().contains("failed when file upload"));
    verify(s3Client, never()).putObject(any(PutObjectRequest.class), any(RequestBody.class));
  }

  @Test
  void test_saveFile_S3Exception() throws IOException {
    // given
    MultipartFile file = mock(MultipartFile.class);
    when(file.getOriginalFilename()).thenReturn("test.txt");
    when(file.getBytes()).thenReturn("test content".getBytes());
    when(fileStorageProperties.getS3Bucket()).thenReturn("test-bucket");
    doThrow(S3Exception.builder().message("S3 error").build()).when(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));

    // when && then
    FileStorageException exception = assertThrows(FileStorageException.class, () -> s3FileStorageAdapter.saveFile(file));
    assertTrue(exception.getMessage().contains("failed when upload file to S3"));
  }

  @Test
  void test_deleteFile() {
    // given
    when(fileStorageProperties.getS3Bucket()).thenReturn("test-bucket");

    // when
    s3FileStorageAdapter.deleteFile("test.txt");

    // then
    verify(s3Client, times(1)).deleteBucket(any(DeleteBucketRequest.class));
  }

  @Test
  void test_deleteFile_S3Exception() {
    // given
    when(fileStorageProperties.getS3Bucket()).thenReturn("test-bucket");
    doThrow(S3Exception.builder().message("S3 error").build()).when(s3Client).deleteBucket(any(DeleteBucketRequest.class));

    // when && then
    FileStorageException exception = assertThrows(FileStorageException.class, () -> s3FileStorageAdapter.deleteFile("test.txt"));
    assertTrue(exception.getMessage().contains("failed when delete file in s3"));
  }
}