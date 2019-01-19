package com.github.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
    
    //findByShortUrl
    
    //getAccessCount
    
    //redirect
    @RequestMapping(value = "/redirect/{shortURL}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> redirect(@PathVariable("shortURL") String shortURL) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        ShortenedURL shortenedURL = shortenedURLService.findByShortUrl(shortURL);
        if (null != shortenedURL) {
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
        shortenedURLService.incrementAccessCount(shortURL);
        URI uri = new URI(shortenedURL.getUrl());
        headers.setLocation(uri);
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }
}
