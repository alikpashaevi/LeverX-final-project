package alik.leverxfinalproject.constants;

public class AuthorizationConstants {
    public static final String ADMIN = "hasAuthority('ROLE_ADMIN')";
    public static final String SELLER = "hasAuthority('ROLE_SELLER')";
    public static final String SELLER_OR_ADMIN = "hasAnyAuthority('ROLE_SELLER', 'ROLE_ADMIN')";
}
