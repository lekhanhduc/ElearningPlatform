package vn.khanhduc.identityservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.khanhduc.identityservice.service.RedisService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "REDIS-SERVICE")
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveToken(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String getToken(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void save(String key, String value, long duration, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, duration, timeUnit);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
