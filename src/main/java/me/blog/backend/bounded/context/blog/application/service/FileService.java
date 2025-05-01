package me.blog.backend.bounded.context.blog.application.service;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.port.in.service.FileUseCase;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import me.blog.backend.bounded.context.blog.port.out.file.FileStoragePort;
import me.blog.backend.bounded.context.blog.domain.vo.FileUploadResponse;

@Service
@RequiredArgsConstructor
public class FileService implements FileUseCase {
  private final FileStoragePort fileStoragePort;

  public FileUploadResponse saveFile(MultipartFile file) {
    return fileStoragePort.saveFile(file);
  }

  public void deleteFile(String fileId) {
    fileStoragePort.deleteFile(fileId);
  }
}