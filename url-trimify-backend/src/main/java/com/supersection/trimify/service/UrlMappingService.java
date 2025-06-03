package com.supersection.trimify.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.supersection.trimify.dto.ClickEventDTO;
import com.supersection.trimify.dto.UrlMappingDTO;
import com.supersection.trimify.model.ClickEvent;
import com.supersection.trimify.model.UrlMapping;
import com.supersection.trimify.model.User;
import com.supersection.trimify.repository.ClickEventRepository;
import com.supersection.trimify.repository.UrlMappingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UrlMappingService {

  private final UrlMappingRepository urlMappingRepository;
  private final ClickEventRepository clickEventRepository;

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

  public List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
    UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
    if (urlMapping != null) {
      return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end)
          .stream()
          .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()))
          .entrySet().stream()
          .map(entry -> new ClickEventDTO(entry.getKey(), entry.getValue()))
          .collect(Collectors.toList());
    }
    return null;
  }

  public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end) {
    List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);
    List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(
        urlMappings, start.atStartOfDay(), end.plusDays(1).atStartOfDay());

    return clickEvents.stream()
        .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()));
  }

}
