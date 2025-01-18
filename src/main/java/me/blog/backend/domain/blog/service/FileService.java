package me.blog.backend.domain.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import me.blog.backend.adaptors.file.FileStoragePort;
import me.blog.backend.domain.blog.vo.FileUploadResponse;

@Service
public class FileService {
  private final FileStoragePort fileStoragePort;

  public FileService(FileStoragePort fileStoragePort) {
    this.fileStoragePort = fileStoragePort;
  }

  public FileUploadResponse saveFile(MultipartFile file) {
    return fileStoragePort.saveFile(file);
  }

  public void deleteFile(String fileId) {
    fileStoragePort.deleteFile(fileId);
  }
}