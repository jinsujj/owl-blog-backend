package me.blog.backend.bounded.context.blog.adapter.in.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import me.blog.backend.bounded.context.blog.application.service.FileService;
import me.blog.backend.bounded.context.blog.domain.vo.FileUploadResponse;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/files")
public class FileUploadController {
  private final FileService fileUploadService;

  public FileUploadController(FileService fileUploadService) {
    this.fileUploadService = fileUploadService;
  }

  @Operation(
      summary = "Upload an image file",
      description = "Uploads an image file to the server",
      responses = {
          @ApiResponse(responseCode = "200", description = "File uploaded successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FileUploadResponse.class)))
      }
  )
  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FileUploadResponse> uploadImage(@RequestParam("file") MultipartFile file) {
    FileUploadResponse response = fileUploadService.saveFile(file);
    return ResponseEntity.ok(response);
  }


  @Operation(
      summary = "Delete an uploaded image file",
      description = "Deletes an image file from the server",
      responses = {
          @ApiResponse(responseCode = "204", description = "File deleted successfully")
      }
  )
  @DeleteMapping("/upload")
  public ResponseEntity<Void> deleteImage(
      @RequestParam("fileName")
      @io.swagger.v3.oas.annotations.Parameter(description = "The name of the file to delete", required = true)
      String fileName
  ) {
    fileUploadService.deleteFile(fileName);
    return ResponseEntity.noContent().build();
  }
}
