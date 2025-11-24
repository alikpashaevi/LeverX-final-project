package alik.leverxfinalproject.integration;

import alik.leverxfinalproject.auth.model.RegisterRequest;
import alik.leverxfinalproject.auth.service.RegisterService;
import alik.leverxfinalproject.entity.AppUser;
import alik.leverxfinalproject.entity.Role;
import alik.leverxfinalproject.error.EmailAlreadyInUseException;
import alik.leverxfinalproject.repo.AppUserRepo;
import alik.leverxfinalproject.repo.RoleRepo;
import alik.leverxfinalproject.service.EmailService;
import alik.leverxfinalproject.service.EmailVerificationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class RegisterServiceIntegrationTest {

    @Autowired
    private RegisterService authService;

    @Autowired
    private AppUserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Test
    void register_createsUserAndSendsEmail() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");

        // role must exist in DB for integration test
        Role role = new Role();
        role.setId(2L);
        role.setName("USER");
        roleRepo.save(role);

        // when
        authService.register(request);

        // then
        AppUser user = userRepo.findByEmail("john@example.com");
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john@example.com", user.getEmail());
        assertNotNull(user.getPassword()); // encoded
        assertFalse(user.getPassword().equals("password123"));

        assertEquals(1, user.getRoles().size());
        assertEquals(2L, user.getRoles().iterator().next().getId());

        // verify interactions with external systems
        verify(emailVerificationService, times(1))
                .saveConfirmationToken(anyString(), eq("john@example.com"));

        verify(emailService, times(1))
                .sendConfirmationEmail(eq("john@example.com"), anyString());
    }

    @Test
    void register_whenEmailExists_shouldThrow() {
        // given
        AppUser existing = new AppUser();
        existing.setEmail("jane@example.com");
        userRepo.save(existing);

        RegisterRequest request = new RegisterRequest();
        request.setEmail("jane@example.com");

        // then
        assertThrows(EmailAlreadyInUseException.class, () -> authService.register(request));
    }
}

