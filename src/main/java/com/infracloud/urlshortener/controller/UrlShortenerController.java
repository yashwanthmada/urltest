package com.infracloud.urlshortener.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.infracloud.urlshortener.service.URLShortenerService;


@CrossOrigin(origins = "*")
@RestController
public class UrlShortenerController {

	@Autowired
	URLShortenerService urlService;

	@PostMapping("/")
	public ResponseEntity<String> postURL(@Valid @RequestBody JSONObject urlJson, HttpServletRequest request) {
		return urlService.generateShortURL(urlJson.get("url").toString(), request.getRequestURL().toString());
	}

	@GetMapping("/{shortenedURL}")
	public ResponseEntity<Object> getURL(@PathVariable String shortenedURL) {
		return urlService.redirectShortenedURL(shortenedURL);
	}
}
