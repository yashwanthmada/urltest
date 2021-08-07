package com.infracloud.urlshortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.infracloud.urlshortener.model.URLEntry;

@Repository
public interface URLEntryRepo extends JpaRepository<URLEntry, String> {
}