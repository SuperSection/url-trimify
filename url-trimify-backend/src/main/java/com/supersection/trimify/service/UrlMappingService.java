package com.supersection.trimify.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.supersection.trimify.dto.UrlMappingDTO;
import com.supersection.trimify.model.UrlMapping;
import com.supersection.trimify.model.User;
import com.supersection.trimify.repository.UrlMappingRepository;

@Service
public class UrlMappingService {

  private final UrlMappingRepository urlMappingRepository;

  public UrlMappingService(UrlMappingRepository urlMappingRepository) {
    this.urlMappingRepository = urlMappingRepository;
  }

  public UrlMappingDTO createShortUrl(String originalUrl, User user) {
    String shortUrl = generateShortUrl();

    UrlMapping urlMapping = new UrlMapping();
    urlMapping.setOriginalUrl(originalUrl);
    urlMapping.setShortUrl(shortUrl);
    urlMapping.setUser(user);
    urlMapping.setCreatedAt(LocalDateTime.now());

    UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
    return convertToDto(savedUrlMapping);
  }

  private UrlMappingDTO convertToDto(UrlMapping urlMapping) {
    UrlMappingDTO dto = new UrlMappingDTO();
    dto.setId(urlMapping.getId());
    dto.setOriginalUrl(urlMapping.getOriginalUrl());
    dto.setShortUrl(urlMapping.getShortUrl());
    dto.setClickCount(urlMapping.getClickCount());
    dto.setCreatedAt(urlMapping.getCreatedAt());
    dto.setUsername(urlMapping.getUser().getUsername());

    return dto;
  }

  private String generateShortUrl() {
    String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    Random random = new Random();
    StringBuilder shortUrl = new StringBuilder(8);

    for (int i = 0; i < 8; i++) {
      shortUrl.append(characters.charAt(random.nextInt(characters.length())));
    }

    return shortUrl.toString();
  }

  public List<UrlMappingDTO> getUrlsByUser(User user) {
    return urlMappingRepository.findByUser(user)
        .stream()
        .map(this::convertToDto)
        .toList();
  }

}
