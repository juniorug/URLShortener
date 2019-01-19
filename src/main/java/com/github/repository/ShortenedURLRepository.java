package com.github.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.model.ShortenedURL;

public interface ShortenedURLRepository extends CrudRepository<ShortenedURL, Long>  {

    Optional<ShortenedURL> findById(Long id);
    
    ShortenedURL findByShortUrl(String shortUrl);
    
    @Query("SELECT s.accessCount FROM ShortenedURL s WHERE shortUrl = :inShortUrl")
    int getAccessCount(@Param("inShortUrl") String inShortUrl);
    
    @Query("Update ShortenedURL s Set s.accessCount = s.accessCount + 1 WHERE shortUrl = :inShortUrl")
    void incrementAccessCount(@Param("inShortUrl") String inShortUrl);
}
