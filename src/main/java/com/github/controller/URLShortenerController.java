package com.github.controller;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
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
        System.out.println("getShortenedURLByShortUrl! shortURL:" + shortURL);
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
        System.out.println("redirect! shortURL:" + shortURL);
        HttpHeaders headers = new HttpHeaders();
        ShortenedURL shortenedURL = shortenedURLService.findByShortUrl(shortURL);
        System.out.println("shortURL.url:" + shortenedURL.getUrl());
        System.out.println("shortURL.count:" + shortenedURL.getAccessCount());
        if (null == shortenedURL.getId()) {
            System.out.println("null == shortenedURL");
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
    
    
    
    //Model Methods
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String showForm(ShortenedURL request) {
        return "shortenedurls";
    }
    
   /* @RequestMapping(value="/", method=RequestMethod.POST)
    public ModelAndView getAllShortenedURLModel() {
      System.out.println("getAllShortenedURLModel!");
      return new ModelAndView("shortenedURLs", "shortenedURLs", shortenedURLService.findAll());
    }*/
    
    private boolean isUrlValid(String url) {
        boolean valid = true;
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            valid = false;
        }
        return valid;
    }
    
    @RequestMapping(value="/", method = RequestMethod.POST)
    public ModelAndView shortenUrl(HttpServletRequest httpRequest,
                                   @Valid ShortenedURL request,
                                   BindingResult bindingResult) {
        System.out.println("getAllShortenedURLModel!");
        String url = request.getUrl();
        if (!isUrlValid(url)) {
            bindingResult.addError(new ObjectError("url", "Invalid url format: " + url));
        }

        ModelAndView modelAndView = new ModelAndView("shortenedURLs");
        if (!bindingResult.hasErrors()) {
            
            shortenedURLService.save(new ShortenedURL());
            String requestUrl = httpRequest.getRequestURL().toString();
            String prefix = requestUrl.substring(0, requestUrl.indexOf(httpRequest.getRequestURI(),
                "http://".length()));

            modelAndView.addObject("shortenedURLs", prefix + "/");
        }
        return modelAndView;
    }

    
}
