package alik.leverxfinalproject.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnonymousIdService {


    public String getOrCreateAnonymousId(HttpServletRequest request,
                                         HttpServletResponse response) {

        String anonId = Arrays.stream(Optional.ofNullable(request.getCookies())
                        .orElse(new Cookie[0]))
                .filter(c -> "anon_id".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (anonId != null) return anonId;

        anonId = UUID.randomUUID().toString();

        ResponseCookie cookie = ResponseCookie.from("anon_id", anonId)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(60L * 60 * 24 * 30) // 30 days
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return anonId;
    }

    public String getCurrentAnonymousId(HttpServletRequest request) {
        return Arrays.stream(Optional.ofNullable(request.getCookies())
                        .orElse(new Cookie[0]))
                .filter(c -> "anon_id".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

}
