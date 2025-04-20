package me.blog.backend.bounded.context.blog.domain.vo;

public class FileUploadResponse {
  private final String fileUrl;

  public FileUploadResponse(String fileUrl) {
    this.fileUrl = fileUrl;
  }

  public String getFileUrl() {
    return fileUrl;
  }
}
