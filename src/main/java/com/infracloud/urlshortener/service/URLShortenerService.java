package com.infracloud.urlshortener.service;

import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.infracloud.urlshortener.model.URLEntry;
import com.infracloud.urlshortener.repository.URLEntryRepo;

@Service
public class URLShortenerService {
	
	@Autowired
	URLEntryRepo urlEntryRepo;

	public Long computeHash(String s, int p) {
		long m = (long) (1e9 + 9);
		long hash_value = 0;
		long p_pow = 1;
		for (char c : s.toCharArray()) {
			hash_value = (hash_value + (c - 'a' + 1) * p_pow) % m;
			p_pow = (p_pow * p) % m;
		}
		return hash_value;
	}

	public String hashToShortString(long n) {
		n = Math.abs(n);
		char map[] = "pQjdJzVvPB26frUxb0XKZ1wTSLYEklmO9Rngti7FA4scaMC8GyWHh5NDu3oIqe".toCharArray();
		StringBuffer shorturl = new StringBuffer();
		while (n > 0) {
			shorturl.append(map[(int) (n % 62)]);
			n /= 63;
		}
		return shorturl.reverse().toString();
	}

	public ResponseEntity<String> generateShortURL(String originalURL, String requestURL) {
		try {
			String base64_url = Base64.getEncoder().encodeToString(originalURL.getBytes("utf-8"));
			String shortenedURL = hashToShortString(computeHash(base64_url, 31)) + "-"
					+ hashToShortString(computeHash(base64_url, 43));
			URLEntry urlEntry = new URLEntry(shortenedURL, originalURL);
			urlEntry = urlEntryRepo.save(urlEntry);
			urlEntry.setShortenedURL(requestURL + urlEntry.getShortenedURL());
			return ResponseEntity.ok(urlEntry.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, String> errorMsg = new HashMap<String, String>();
			errorMsg.put("originalURL", originalURL);
			errorMsg.put("Status", "Internal error occured");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(builErrorMessage("Internal Server Error"));
		}
	}

	public ResponseEntity<Object> redirectShortenedURL(String shortenedURL) {
		Optional<URLEntry> urlEntry = urlEntryRepo.findById(shortenedURL);
		if (urlEntry.isPresent()) {
			return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(urlEntry.get().getOriginalURL()))
					.build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(builErrorMessage("URL not found"));
	}

	public String builErrorMessage(String errorMsg) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Message", errorMsg);
		return new JSONObject(map).toJSONString();
	}
}
