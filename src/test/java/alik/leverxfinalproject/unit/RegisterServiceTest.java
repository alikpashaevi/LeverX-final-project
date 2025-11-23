package alik.leverxfinalproject.unit;

import alik.leverxfinalproject.auth.model.RegisterRequest;
import alik.leverxfinalproject.auth.service.RegisterService;
import alik.leverxfinalproject.entity.AppUser;
import alik.leverxfinalproject.entity.Role;
import alik.leverxfinalproject.error.EmailAlreadyInUseException;
import alik.leverxfinalproject.repo.AppUserRepo;
import alik.leverxfinalproject.repo.RoleRepo;
import alik.leverxfinalproject.service.EmailService;
import alik.leverxfinalproject.service.EmailVerificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterServiceTest {

    @Mock
    private AppUserRepo userRepo;

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailVerificationService emailVerificationService;

    @Mock
    private EmailService emailService;

    private RegisterService authService;

    @BeforeEach
    void setUp() {
        authService = new RegisterService(userRepo, passwordEncoder, roleRepo,
                emailService, emailVerificationService);
    }

    @Test
    void register_SuccessfullyCreatesUser_AndSendsEmail() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@example.com");
        request.setPassword("pass123");

        Role role = new Role();
        role.setId(2L);
        role.setName("USER");

        AppUser savedUser = new AppUser();
        savedUser.setId(1L);
        savedUser.setEmail("john@example.com");

        when(userRepo.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("hashed-pass");
        when(roleRepo.findById(2L)).thenReturn(Optional.of(role));
        when(userRepo.save(any(AppUser.class))).thenReturn(savedUser);

        authService.register(request);

        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(userRepo).save(userCaptor.capture());
        AppUser capturedUser = userCaptor.getValue();

        assertEquals("John", capturedUser.getFirstName());
        assertEquals("Doe", capturedUser.getLastName());
        assertEquals("john@example.com", capturedUser.getEmail());
        assertEquals("hashed-pass", capturedUser.getPassword());
        assertTrue(capturedUser.getRoles().contains(role));

        verify(emailVerificationService).saveConfirmationToken(anyString(), eq("john@example.com"));

        verify(emailService).sendConfirmationEmail(eq("john@example.com"), anyString());
    }

    @Test
    void register_ThrowsException_WhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("taken@example.com");

        when(userRepo.existsByEmail("taken@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyInUseException.class,
                () -> authService.register(request));

        verify(userRepo, never()).save(any());
        verify(emailService, never()).sendConfirmationEmail(any(), any());
    }

}
