package me.blog.backend.bounded.context.blog.port.out;

import org.springframework.web.multipart.MultipartFile;

import me.blog.backend.bounded.context.blog.domain.vo.FileUploadResponse;

public interface FileStoragePort {
  FileUploadResponse saveFile(MultipartFile file);
  void deleteFile(String fileName);
}
