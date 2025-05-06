package me.blog.backend.bounded.context.blog.adapter.out.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import me.blog.backend.bounded.context.blog.port.out.file.FileStoragePort;
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

    String uploadDir     = fileStorageProperties.getUploadDir();
    Path directoryPath = Paths.get(uploadDir);

    try{
      // create directory
      if(!Files.exists(directoryPath))
        Files.createDirectories(directoryPath);

      // splitting the original filename and extension
      String originalName = Objects.requireNonNull(file.getOriginalFilename(), "file name is null")
              .replace(' ', '_');

      int dotIdx = originalName.lastIndexOf('.');
      String baseName = (dotIdx == -1) ? originalName : originalName.substring(0, dotIdx);
      String ext      = (dotIdx == -1) ? ""           : originalName.substring(dotIdx); // ".png"

      // incrementing the version number on conflict
      Path filePath = directoryPath.resolve(originalName);
      int index = 1;

      while (Files.exists(filePath)) {
        String candidate = baseName + "(" + index + ")" + ext;
        filePath = directoryPath.resolve(candidate);
        index++;
      }

      // file save
      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
      }

      // response image path url
      String fileUrl = fileStorageProperties.getBaseUrl() + "/" + filePath.getFileName();
      return new FileUploadResponse(fileUrl);
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
