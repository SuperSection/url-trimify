package com.supersection.trimify.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.supersection.trimify.model.ClickEvent;
import com.supersection.trimify.model.UrlMapping;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {

  List<ClickEvent> findByUrlMappingAndClickDateBetween(
      UrlMapping mapping, LocalDateTime startDate, LocalDateTime endDate);

  List<ClickEvent> findByUrlMappingInAndClickDateBetween(
      List<UrlMapping> urlMappings, LocalDateTime startDate, LocalDateTime endDate);
}
