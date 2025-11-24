package alik.leverxfinalproject.components;

import org.springframework.security.core.context.SecurityContextHolder;

public class GetUserIdFromToken {
    public static long getUserIdFromToken() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
