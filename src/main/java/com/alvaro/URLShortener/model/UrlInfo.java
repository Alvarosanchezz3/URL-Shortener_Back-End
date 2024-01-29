package com.alvaro.URLShortener.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "url_info")
public class UrlInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "long_url")
    private String longUrl;

    @Column(name = "short_url")
    private String shortUrl;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}