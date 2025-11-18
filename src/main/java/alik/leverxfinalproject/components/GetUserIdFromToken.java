package alik.leverxfinalproject.components;

import org.springframework.security.core.context.SecurityContextHolder;

public class GetUserIdFromToken {
    public static String getUserIdFromToken() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
