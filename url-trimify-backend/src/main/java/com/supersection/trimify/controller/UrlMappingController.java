package com.supersection.trimify.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.supersection.trimify.dto.ClickEventDTO;
import com.supersection.trimify.dto.UrlMappingDTO;
import com.supersection.trimify.model.User;
import com.supersection.trimify.service.UrlMappingService;
import com.supersection.trimify.service.UserService;


@RestController
@RequestMapping("/api/urls")
public class UrlMappingController {

  private final UrlMappingService urlMappingService;
  private final UserService userService;

  public UrlMappingController(UrlMappingService urlMappingService, UserService userService) {
    this.urlMappingService = urlMappingService;
    this.userService = userService;
  }

  /**
   * Creates a short URL for the given original URL.
   *
   * @param request   A map containing the original URL under the key "originalUrl".
   * @param principal The authenticated user principal.
   * @return A ResponseEntity containing the created UrlMappingDTO.
   */
  @PostMapping("/trimify")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UrlMappingDTO> createShortUrl(
      @RequestBody Map<String, String> request, Principal principal
  ) {
    String originalUrl = request.get("originalUrl");
    User user = userService.findByUsername(principal.getName());

    UrlMappingDTO urlMappingDTO = urlMappingService.createShortUrl(originalUrl, user);
    return ResponseEntity.ok(urlMappingDTO);
  }


  @GetMapping("/myurls")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<UrlMappingDTO>> getMyUrls(Principal principal) {
    User user = userService.findByUsername(principal.getName());
    List<UrlMappingDTO> urls = urlMappingService.getUrlsByUser(user);
    return ResponseEntity.ok(urls);
  }


  @GetMapping("/analytics/{shortUrl}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<ClickEventDTO>> getUrlAnalytics(
      @PathVariable String shortUrl,
      @RequestParam("startDate") String startDate,
      @RequestParam("endDate") String endDate
  ) {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    LocalDateTime start = LocalDateTime.parse(startDate, formatter);
    LocalDateTime end = LocalDateTime.parse(endDate, formatter);
    List<ClickEventDTO> clickEventAnalytics = urlMappingService.getClickEventsByDate(shortUrl, start, end);
    return ResponseEntity.ok(clickEventAnalytics);
  }

}
