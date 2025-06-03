package com.supersection.trimify.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
