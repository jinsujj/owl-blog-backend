package me.blog.backend.bounded.context.blog.adapter.out.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import me.blog.backend.bounded.context.blog.port.out.FileStoragePort;
import org.springframework.web.multipart.MultipartFile;

import me.blog.backend.common.exception.FileStorageException;
import me.blog.backend.bounded.context.blog.domain.vo.FileUploadResponse;

public class LocalFileStorageAdapter implements FileStoragePort {
  private final FileStorageProperties fileStorageProperties;

  public LocalFileStorageAdapter(FileStorageProperties fileStorageProperties) {
    this.fileStorageProperties = fileStorageProperties;
  }

  public FileUploadResponse saveFile(MultipartFile file) {
    if(file.isEmpty())
      throw new FileStorageException("file is Empty");

    try{
      // create directory
      String uploadDir = fileStorageProperties.getUploadDir();
      Path directoryPath = Paths.get(uploadDir);

      if(!Files.exists(directoryPath))
        Files.createDirectories(directoryPath);


      // save file
      String fileName = Objects.requireNonNull(file.getOriginalFilename(), "file name is null")
              .replace(' ', '_');

      Path filePath = directoryPath.resolve(fileName);
      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
      }

      return new FileUploadResponse(fileStorageProperties.getBaseUrl() +"/"+ fileName);
    }
    catch (IOException e){
      throw new FileStorageException("unexpected error happened during upload "+ e.getMessage());
    }
  }

  @Override
  public void deleteFile(String fileName) {
    try{
      String uploadDir = fileStorageProperties.getUploadDir();
      Path filePath = Paths.get(uploadDir).resolve(fileName);

      if(!Files.exists(filePath))
        throw new FileStorageException("file not found "+ fileName);

      Files.delete(filePath);
    }
    catch (IOException e){
      throw new FileStorageException("unexpected error happened during delete",e);
    }
  }
}
