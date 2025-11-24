package alik.leverxfinalproject.auth.controller;

import alik.leverxfinalproject.auth.model.LoginRequest;
import alik.leverxfinalproject.auth.service.JwtService;
import alik.leverxfinalproject.auth.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/login")
//@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    @PostMapping
    public ResponseEntity<JwtService.LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(loginService.login(loginRequest));
    }

}
