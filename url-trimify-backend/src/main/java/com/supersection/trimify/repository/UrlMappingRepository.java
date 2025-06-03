package com.supersection.trimify.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.supersection.trimify.model.UrlMapping;
import com.supersection.trimify.model.User;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

  UrlMapping findByShortUrl(String shortUrl);

  List<UrlMapping> findByUser(User user);
}
