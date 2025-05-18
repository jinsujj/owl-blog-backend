package me.blog.backend.bounded.context.blog.adapter.in.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.application.service.BlogService;
import me.blog.backend.bounded.context.blog.domain.vo.BlogVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class SitemapController {
    private final BlogService blogService;
    private final HttpServletRequest request;

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String generateSitemap() {
        setSiteMapVisitorLog();
        List<BlogVO> allBlogs = blogService.getAllBlogs().stream().filter(s -> s.publishedAt() != null).toList();
        StringBuilder sb = new StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"\n");
        sb.append("        xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\">\n");

        for (BlogVO blog : allBlogs) {
            sb.append("<url>\n");
            sb.append("    <loc>").append("https://owl-dev.me/blog/").append(blog.id()).append("</loc>\n");
            sb.append("    <lastmod>").append(blog.updatedAt().toLocalDate()).append("</lastmod>\n");

            if (blog.thumbnailUrl() != null) {
                sb.append("    <image:image>\n");
                sb.append("      <image:loc>").append(blog.thumbnailUrl()).append("</image:loc>\n");
                sb.append("      <image:title>").append(escapeXml(blog.title())).append("</image:title>\n");
                String summary = Optional.ofNullable(blog.summary()).orElse(blog.title());
                sb.append("      <image:caption>").append(escapeXml(summary)).append("</image:caption>\n");
                sb.append("    </image:image>\n");
            }
            sb.append("</url>\n");
        }
        sb.append("</urlset>\n");
        return sb.toString();
    }

    private void setSiteMapVisitorLog() {
        String ipAddress = request.getHeader("X-Forwarded-For");
        String remoteAddr = request.getRemoteAddr();
        String clientIp = (ipAddress != null && !ipAddress.isEmpty())
                ? ipAddress.split(",")[0].trim()
                : remoteAddr;

        System.out.printf(
                "Sitemap [Visitor IP] %s%n",
                 clientIp);
    }

    private String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
