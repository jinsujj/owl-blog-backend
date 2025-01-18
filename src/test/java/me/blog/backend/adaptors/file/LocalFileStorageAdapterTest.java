package me.blog.backend.adaptors.file;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.web.multipart.MultipartFile;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.blog.backend.common.exception.FileStorageException;
import me.blog.backend.domain.blog.vo.FileUploadResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LocalFileStorageAdapterTest {
  @Mock
  private FileStorageProperties fileStorageProperties;

  @InjectMocks
  private LocalFileStorageAdapter localFileStorageAdapter;

  @BeforeEach
  void setUp(){
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void test_saveFile() throws IOException {
    // given
    String uploadDir = "src/main/resources/static/uploads";
    String baseUrl ="http://localhost:8080/static";
    String fileName ="test.txt";

    MultipartFile file = mock(MultipartFile.class);
    when(file.isEmpty()).thenReturn(false);
    when(file.getOriginalFilename()).thenReturn(fileName);
    when(file.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes()));
    when(fileStorageProperties.getUploadDir()).thenReturn(uploadDir);
    when(fileStorageProperties.getBaseUrl()).thenReturn(baseUrl);

    // when
    FileUploadResponse response = localFileStorageAdapter.saveFile(file);

    // then
    assertNotNull(response);
    assertEquals(baseUrl+"/"+fileName, response.getFileUrl());

    // cleanup
    Path filePath = Paths.get(uploadDir, fileName);
    Files.deleteIfExists(filePath);
  }

  @Test
  void test_saveFile_shoud_throw_exception_when_file_isEmpty() throws IOException {
    // given
    MultipartFile file = mock(MultipartFile.class);
    when(file.isEmpty()).thenReturn(true);

    // when
    FileStorageException exception = assertThrows(FileStorageException.class, () -> localFileStorageAdapter.saveFile(file));

    // then
    assertEquals("file is Empty", exception.getMessage());
  }

  @Test
  void test_deleteFile() throws IOException {
    // given
    String uploadDir = "src/main/resources/static/uploads";
    String fileName = "test.txt";

    Path directoryPath = Paths.get(uploadDir);
    Files.createDirectories(directoryPath);
    Path filePath = directoryPath.resolve(fileName);
    Files.createFile(filePath);

    when(fileStorageProperties.getUploadDir()).thenReturn(uploadDir);

    // when
    localFileStorageAdapter.deleteFile(fileName);

    // then
    assertFalse(Files.exists(filePath));
  }

  @Test
  void test_deleteFile_shoud_throw_exception_when_file_isEmpty() throws IOException {
    // given
    String uploadDir = "src/main/resources/static/uploads";
    String fileName = "test.txt";
    when(fileStorageProperties.getUploadDir()).thenReturn(uploadDir);

    // when
    FileStorageException exception = assertThrows(FileStorageException.class, () ->
        localFileStorageAdapter.deleteFile(fileName)
    );

    // then
    assertEquals(String.format("file not found "+fileName) , exception.getMessage());
  }
}