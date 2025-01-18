package me.blog.backend.domain.blog.vo;

public class FileUploadResponse {
  private final String fileUrl;

  public FileUploadResponse(String fileUrl) {
    this.fileUrl = fileUrl;
  }

  public String getFileUrl() {
    return fileUrl;
  }
}
