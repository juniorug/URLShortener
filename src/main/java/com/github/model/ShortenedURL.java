package com.github.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="ShortenedURL")
public class ShortenedURL {
    
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Size(min = 2, max = 1024)
    @Column(name="shortUrl", unique=true)
    private String shortUrl;
    
    @NotNull
    @Size(min = 5, max = 1024)
    @Column(name="url")
    private String url;

    @Column(name="accessCount")
    private int accessCount = 0;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public int getAccessCount() {
        return accessCount;
    }

}
