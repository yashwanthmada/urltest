package com.infracloud.urlshortener.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.json.simple.JSONObject;

@Entity
public class URLEntry {
	
	@Id
	private String shortenedURL;
	@Column(unique = true, updatable = false, nullable = false)
	private String originalURL;

	public URLEntry() {
		super();
	}

	public URLEntry(String shortenedURL, String originalURL) {
		super();
		this.originalURL = originalURL;
		this.shortenedURL = shortenedURL;
	}

	public String getOriginalURL() {
		return originalURL;
	}

	public void setOriginalURL(String originalURL) {
		this.originalURL = originalURL;
	}

	public String getShortenedURL() {
		return shortenedURL;
	}

	public void setShortenedURL(String shortenedURL) {
		this.shortenedURL = shortenedURL;
	}

	@Override
	public String toString() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("shortenedURL", shortenedURL);
		map.put("originalURL", originalURL);
		return new JSONObject(map).toJSONString();
	}

}
