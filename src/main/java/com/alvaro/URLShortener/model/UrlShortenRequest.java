package com.alvaro.URLShortener.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class UrlShortenRequest {
    private String longUrl;
    private String description;
    private String email;
}
