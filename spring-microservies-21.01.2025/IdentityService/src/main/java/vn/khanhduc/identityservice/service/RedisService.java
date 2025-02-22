package vn.khanhduc.identityservice.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {
    void saveToken(String key, String value);
    String getToken(String key);
    void save(String key, String value, long duration, TimeUnit timeUnit);
    void delete(String key);
}
