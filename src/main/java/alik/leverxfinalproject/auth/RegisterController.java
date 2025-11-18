package alik.leverxfinalproject.auth;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest registerRequest) {
        registerService.register(registerRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend_confirmation")
    public ResponseEntity<Void> resendConfirmationEmail(@RequestParam("email") String email) {
        registerService.resendConfirmationEmail(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/confirm_email")
    public ResponseEntity<Void> confirmEmail(@RequestParam("token") String token) {
        registerService.confirmEmail(token);
        System.out.println("Email confirmed for token: " + token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<Void> forgotPassword(@RequestParam("email") String email) {
        registerService.sendPasswordResetEmail(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset_password")
    public ResponseEntity<Void> resetPassword(@RequestParam("code") String code,
                                              @RequestParam("new_password") String newPassword) {
        registerService.checkCodeAndResetPassword(code, newPassword);
        return ResponseEntity.ok().build();
    }

}
