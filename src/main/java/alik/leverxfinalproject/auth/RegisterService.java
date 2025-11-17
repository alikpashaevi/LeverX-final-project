package alik.leverxfinalproject.auth;

import alik.leverxfinalproject.entity.AppUser;
import alik.leverxfinalproject.repo.AppUserRepo;
import alik.leverxfinalproject.repo.RoleRepo;
import alik.leverxfinalproject.service.EmailService;
import alik.leverxfinalproject.service.EmailVerificationService;
import alik.leverxfinalproject.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RegisterService {

    private final AppUserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    // TODO: replace it with role service later
    private final RoleRepo roleRepo;
    private final EmailService emailService;
    private final EmailVerificationService emailVerificationService;

    public RegisterService(AppUserRepo userRepo, PasswordEncoder passwordEncoder, RoleRepo roleRepo, EmailService emailService, EmailVerificationService emailVerificationService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
        this.emailService = emailService;
        this.emailVerificationService = emailVerificationService;
    }

    public void register(RegisterRequest registerRequest) {
        if (userRepo.existsByEmail(registerRequest.getEmail())) {
            // TODO: replace with custom exception
            throw new RuntimeException("Email already in use");
        }
        AppUser appUser = new AppUser();
        appUser.setFirstName(registerRequest.getFirstName());
        appUser.setLastName(registerRequest.getLastName());
        appUser.setEmail(registerRequest.getEmail());
        appUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        appUser.setRoles(roleRepo.findById(2L).stream().collect(Collectors.toSet()));

        AppUser savedUser = userRepo.save(appUser);

        String token = UUID.randomUUID().toString();

        emailVerificationService.saveConfirmationToken(token, savedUser.getEmail());

        emailService.sendConfirmationEmail(savedUser.getEmail(), token);

    }

    public void resendConfirmationEmail(String email) {
        AppUser user = userRepo.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User with email not found");
        }
        if (user.isEmailVerified()) {
            throw new RuntimeException("Email is already verified");
        }

        String token = UUID.randomUUID().toString();
        emailVerificationService.saveConfirmationToken(token, email);
        emailService.sendConfirmationEmail(email, token);
    }

    public void confirmEmail(String token) {
        String email = emailVerificationService.getEmailByToken(token);

        if (email == null) {
            throw new RuntimeException("Invalid token");
        }

        AppUser user = userRepo.findByEmail(email);
        user.setEmailVerified(true);
        userRepo.save(user);
        emailVerificationService.deleteToken(token);
    }

    public void sendPasswordResetEmail(String email) {

        int resetCode = (int) (Math.random() * 900000) + 100000;
        String resetCodeStr = String.valueOf(resetCode);
        System.out.println("Generated reset code: " + resetCodeStr);
        System.out.println("Sending password reset email to: " + email);

        emailVerificationService.savePasswordResetCode(resetCodeStr, email);
        emailService.sendPasswordResetEmail(email, resetCodeStr);

    }

    public void checkCodeAndResetPassword(String code, String newPassword) {
        String email = emailVerificationService.getEmailByResetCode(code);

        if (email == null) {
            throw new RuntimeException("Invalid reset code");
        }

        AppUser user = userRepo.getAppUserByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

}
