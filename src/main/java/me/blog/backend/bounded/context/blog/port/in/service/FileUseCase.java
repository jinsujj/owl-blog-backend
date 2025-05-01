package me.blog.backend.bounded.context.blog.port.in.service;

import me.blog.backend.bounded.context.blog.domain.vo.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileUseCase {
    FileUploadResponse saveFile(MultipartFile file);
    void deleteFile(String fileId);
}
