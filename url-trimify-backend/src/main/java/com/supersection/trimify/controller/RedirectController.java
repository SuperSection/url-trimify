package com.supersection.trimify.controller;


import com.supersection.trimify.model.UrlMapping;
import com.supersection.trimify.service.UrlMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedirectController {

  private final UrlMappingService urlMappingService;

  @GetMapping("/{shortUrl}")
  public ResponseEntity<Void> redirectToUrl(@PathVariable String shortUrl) {
    UrlMapping urlMapping = urlMappingService.getOriginalUrl(shortUrl);

    if (urlMapping != null) {
      // Redirect to the original URL
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.add("Location", urlMapping.getOriginalUrl());
      return ResponseEntity.status(HttpStatus.FOUND).headers(httpHeaders).build();

    } else {
      return ResponseEntity.notFound().build();
    }
  }

}
