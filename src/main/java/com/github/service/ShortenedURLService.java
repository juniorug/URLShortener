package com.github.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.github.model.ShortenedURL;

@Component("ShortenedURLService")
public interface ShortenedURLService {

    void save(ShortenedURL shortenedURL);
    
    Optional<ShortenedURL> findById(Long id);
    
    ShortenedURL findByShortUrl(String shortUrl);
    
    Integer getAccessCount(String shortUrl);
    
    String getUrl(String shortUrl);
    
    void incrementAccessCount(String shortUrl);
    
    List<ShortenedURL> findAll();
}
