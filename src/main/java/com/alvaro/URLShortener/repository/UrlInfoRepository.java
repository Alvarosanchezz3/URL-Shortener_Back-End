package com.alvaro.URLShortener.repository;

import com.alvaro.URLShortener.model.UrlInfo;
import com.alvaro.URLShortener.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlInfoRepository extends JpaRepository<UrlInfo, Long> {
    UrlInfo findByShortUrl(String shortUrl);
    UrlInfo findByLongUrlAndUser(String longUrl, User user);

    List<UrlInfo> findByUser(User userId);
}
