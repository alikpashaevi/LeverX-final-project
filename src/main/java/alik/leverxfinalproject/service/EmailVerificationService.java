package alik.leverxfinalproject.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class EmailVerificationService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final Duration EXPIRATION = Duration.ofHours(24);

    public EmailVerificationService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveConfirmationToken(String token, String email) {
        String key = "email_verification:" + token;
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, email, EXPIRATION);
    }

    public String getEmailByToken(String token) {
        String key = "email_verification:" + token;
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return (String) ops.get(key);
    }

    public void deleteToken(String token) {
        String key = "email_verification:" + token;
        redisTemplate.delete(key);
    }

    public boolean isTokenValid(String token) {
        return getEmailByToken(token) != null;
    }



}
