package alik.leverxfinalproject.auth;

import alik.leverxfinalproject.error.InvalidLoginException;
import alik.leverxfinalproject.entity.AppUser;
import alik.leverxfinalproject.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class LoginService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        AppUser appUser = userService.getUserByEmail(loginRequest.getEmail());

        if (appUser != null && passwordEncoder.matches(loginRequest.getPassword(), appUser.getPassword()) && appUser.isVerified() && appUser.isEmailVerified()) {
            return jwtService.generateLoginResponse(appUser);
        }
        throw new InvalidLoginException("Invalid credentials or user not verified");
    }

}
