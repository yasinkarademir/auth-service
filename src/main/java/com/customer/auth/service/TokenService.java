package com.customer.auth.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;

@Service
public class TokenService {

    private final RedisTemplate<String, Object> redisTemplate;


    public TokenService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveToken(String token, String userName) {
        redisTemplate.opsForValue().set(token, userName, Duration.ofMinutes(30));
    }

    public void saveRefreshToken(String refToken, String userName) {
        redisTemplate.opsForValue().set(refToken, userName, Duration.ofHours(8));
    }

    public String getUserNameByToken(String token) {
        return (String) redisTemplate.opsForValue().get(token);
    }

    public boolean isRefTokenValid(String refToken) {
        return redisTemplate.opsForValue().get(refToken) != null;
    }

    //This method can be used for both token and refresh token invalidation.
    public void invalidateToken(String... tokens) {
        Arrays.stream(tokens)
                .forEach(this::invalidateSingleToken);
    }

    private void invalidateSingleToken(String token) {
        if (token != null) {
            redisTemplate.opsForValue().getAndDelete(token);
        }
    }


}
