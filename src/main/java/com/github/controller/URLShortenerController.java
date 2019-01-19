package com.github.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.github.exception.NotFoundException;
import com.github.model.ShortenedURL;
import com.github.service.ShortenedURLService;


@RestController
public class URLShortenerController {
	
	@Autowired
    private ShortenedURLService shortenedURLService;
	
    @RequestMapping(value = "/shortenedURL", method = RequestMethod.POST)
    public ResponseEntity<?> addShortenedURL(@Valid  @RequestBody ShortenedURL shortenedURL, UriComponentsBuilder ucBuilder) {
    	HttpHeaders headers = new HttpHeaders();
    	if (shortenedURLService.findById(shortenedURL.getId()).isPresent()) {
    		return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }

    	shortenedURLService.save(shortenedURL);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/getShortenedURLByShortUrl", method = RequestMethod.GET, produces = "application/json")
    public ShortenedURL getShortenedURLByShortUrl(@RequestParam("shortURL") String shortURL) {
        ShortenedURL shortenedURL = shortenedURLService.findByShortUrl(shortURL);
        if (null == shortenedURL) {
            throw new NotFoundException("shortenedURL not found");
        }
        return shortenedURL;
    }

    @RequestMapping(value = "/incrementAccessCount", method = RequestMethod.POST)
    public void incrementAccessCount(@Valid  @RequestBody ShortenedURL shortenedURL) {
        shortenedURLService.incrementAccessCount(shortenedURL.getShortUrl());
    }
    
    @RequestMapping(value = "/shortenedURL/accessCount", method = RequestMethod.GET, produces = "application/json")
    public Integer getAccessCount(@RequestParam("shortURL") String shortURL) {
        Integer count = shortenedURLService.getAccessCount(shortURL);
        try {
            if (0 == count) {
                // no shortURL found
            }
        } catch(NullPointerException e) {
            throw new NotFoundException("shortenedURL not found");
        }
        return count;
    }
    
    @RequestMapping(value = "/redirect", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> redirect(@RequestParam("shortURL") String shortURL) throws URISyntaxException {
        return redirectPage(shortURL);
    }
    
    
    @RequestMapping(value = "/redirect", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> redirect(@Valid  @RequestBody ShortenedURL shortenedURL) throws URISyntaxException {
        return redirectPage(shortenedURL.getShortUrl());
    }
    
    private ResponseEntity<?> redirectPage(String shortURL) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        ShortenedURL shortenedURL = shortenedURLService.findByShortUrl(shortURL);
        if (null == shortenedURL.getId()) {
            return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
        }
        shortenedURLService.incrementAccessCount(shortenedURL.getShortUrl());
        URI uri = new URI(shortenedURL.getUrl());
        headers.setLocation(uri);
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }
    
    @RequestMapping(value = "/shortenedURL/url", method = RequestMethod.GET, produces = "application/json")
    public String getUrl(@RequestParam("shortURL") String shortURL) {
        String url = shortenedURLService.getUrl(shortURL);
        try {
            if (null == url) {
                // no shortURL found
            }
        } catch(NullPointerException e) {
            throw new NotFoundException("shortenedURL not found");
        }
        return url;
    }
    
    @RequestMapping(value = "/shortenedURL", method = RequestMethod.GET, produces = "application/json")
    public List<ShortenedURL> getAllShortenedURL() {
        List<ShortenedURL> shortenedURLList = new ArrayList<ShortenedURL>();
        shortenedURLList = shortenedURLService.findAll();
        Collections.sort(shortenedURLList, (e1, e2) -> e1.getId().compareTo(e2.getId())); ;
        return shortenedURLList;
    }
 
}
