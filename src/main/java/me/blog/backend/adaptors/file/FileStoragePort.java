package me.blog.backend.adaptors.file;

import org.springframework.web.multipart.MultipartFile;

import me.blog.backend.domain.blog.vo.FileUploadResponse;

public interface FileStoragePort {
  FileUploadResponse saveFile(MultipartFile file);
  void deleteFile(String fileName);
}
