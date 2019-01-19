package com.github.service;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.github.model.ShortenedURL;

@Component("ShortenedURLService")
public interface ShortenedURLService {

    void save(ShortenedURL shortenedURL);
    
    Optional<ShortenedURL> findById(Long id);
    
    ShortenedURL findByShortUrl(String shortUrl);
    
    int getAccessCount(String shortUrl);
    
    void incrementAccessCount(String shortUrl);
    
    //redirect
    
}
