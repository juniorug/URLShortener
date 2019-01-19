package com.github.controller;

import java.net.URI;
import java.net.URISyntaxException;

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
    
    //findByShortUrl

    //getAccessCount
    @RequestMapping(value = "/incrementAccessCount", method = RequestMethod.POST)
    public void incrementAccessCount(@Valid  @RequestBody ShortenedURL shortenedURL) {
        System.out.println("incrementAccessCount! getShortUrl:" + shortenedURL.getShortUrl());
        /*shortURL = (null == shortURL)? "bit.ly" : shortURL;
        System.out.println("incrementAccessCount! shortURL:" + shortURL);*/
        shortenedURLService.incrementAccessCount(shortenedURL.getShortUrl());
    }
    //getAccessCount
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
    
    //redirect
    @RequestMapping(value = "/redirect", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> redirect(@Valid  @RequestBody ShortenedURL shortenedURL) throws URISyntaxException {
        System.out.println("redirect! shortURL:" + shortenedURL.getShortUrl());
        HttpHeaders headers = new HttpHeaders();
        shortenedURL = shortenedURLService.findByShortUrl(shortenedURL.getShortUrl());
        System.out.println("shortURL.url:" + shortenedURL.getUrl());
        System.out.println("shortURL.count:" + shortenedURL.getAccessCount());
        if (null == shortenedURL.getId()) {
            System.out.println("null == shortenedURL");
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
        shortenedURLService.incrementAccessCount(shortenedURL.getShortUrl());
        URI uri = new URI(shortenedURL.getUrl());
        headers.setLocation(uri);
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }
}
