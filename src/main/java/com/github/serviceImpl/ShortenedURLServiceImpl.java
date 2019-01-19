package com.github.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.model.ShortenedURL;
import com.github.repository.ShortenedURLRepository;
import com.github.service.ShortenedURLService;

public class ShortenedURLServiceImpl implements ShortenedURLService {

    @Autowired
    private ShortenedURLRepository shortenedURLRepository;
    
    @Override
    public void save(ShortenedURL shortenedURL) {
        shortenedURLRepository.save(shortenedURL); 
    }
    
    @Override
    public Optional<ShortenedURL> findById(Long id) {
        return shortenedURLRepository.findById(id);
    }

    @Override
    public ShortenedURL findByShortUrl(String shortUrl) {
        return shortenedURLRepository.findByShortUrl(shortUrl);
    }

    @Override
    public int getAccessCount(String shortUrl) {
        return shortenedURLRepository.getAccessCount(shortUrl);
    }

    @Override
    public void incrementAccessCount(String shortUrl) {
        shortenedURLRepository.incrementAccessCount(shortUrl);
    }

}
