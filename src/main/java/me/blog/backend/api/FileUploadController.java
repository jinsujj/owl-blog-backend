package me.blog.backend.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import me.blog.backend.domain.blog.service.FileService;
import me.blog.backend.domain.blog.vo.FileUploadResponse;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/upload")
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
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
  @DeleteMapping
  public ResponseEntity<Void> deleteImage(
      @RequestParam("fileName")
      @io.swagger.v3.oas.annotations.Parameter(description = "The name of the file to delete", required = true)
      String fileName
  ) {
    fileUploadService.deleteFile(fileName);
    return ResponseEntity.noContent().build();
  }
}
