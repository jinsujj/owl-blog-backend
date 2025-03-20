package me.blog.backend.api;

import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/link-preview")
public class LinkPreviewController {
  @GetMapping
  public ResponseEntity<?> getLinkPreview(@RequestParam("url") String url) {
    try {
      Document doc = Jsoup.connect(url).get();

      String title = doc.title();

      String description = "";
      Elements metaDescription = doc.select("meta[name=description]");
      if (!metaDescription.isEmpty()) {
        description = metaDescription.first().attr("content");
      } else {
        Elements ogDescription = doc.select("meta[property=og:description]");
        if (!ogDescription.isEmpty()) {
          description = ogDescription.first().attr("content");
        }
      }

      String image = "";
      Elements metaImage = doc.select("meta[property=og:image]");
      if (!metaImage.isEmpty()) {
        image = metaImage.first().attr("content");
      }

      Map<String, String> response = new HashMap<>();
      response.put("title", title);
      response.put("description", description);
      response.put("image", image);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error fetching link preview: " + e.getMessage());
    }
  }
}
