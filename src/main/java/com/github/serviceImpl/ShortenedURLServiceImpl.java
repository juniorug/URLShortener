package com.github.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.model.ShortenedURL;
import com.github.repository.ShortenedURLRepository;
import com.github.service.ShortenedURLService;


@Service
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
    public Integer getAccessCount(String shortUrl) {
        return shortenedURLRepository.getAccessCount(shortUrl);
    }
    
    @Override
    public String getUrl(String shortUrl) {
        return shortenedURLRepository.getUrl(shortUrl);
    }

    @Override
    public void incrementAccessCount(String shortUrl) {
        shortenedURLRepository.incrementAccessCount(shortUrl);
    }
    
    @Override
    public List<ShortenedURL> findAll() {
        return shortenedURLRepository.findAll();
    }

}
