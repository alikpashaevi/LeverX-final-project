package alik.leverxfinalproject.auth;

import alik.leverxfinalproject.entity.AppUser;
import alik.leverxfinalproject.repo.AppUserRepo;
import alik.leverxfinalproject.service.UserService;
import lombok.RequiredArgsConstructor;
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

        if (passwordEncoder.matches(loginRequest.getPassword(), appUser.getPassword())) {
            return jwtService.generateLoginResponse(appUser);
        }
        // TODO: replace with custom exception
        throw new RuntimeException("Invalid email or password");
    }

}
