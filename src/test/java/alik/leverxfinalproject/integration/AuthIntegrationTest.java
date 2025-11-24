package alik.leverxfinalproject.integration;

import alik.leverxfinalproject.entity.AppUser;
import alik.leverxfinalproject.repo.AppUserRepo;
import alik.leverxfinalproject.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AuthIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.3");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepo appUserRepo;

    @MockitoBean
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUserFlow() throws Exception {
        Map<String, String> registerRequest = new HashMap<>();
        registerRequest.put("firstName", "John");
        registerRequest.put("lastName", "Doe");
        registerRequest.put("email", "john.doe@example.com");
        registerRequest.put("password", "Password123!");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        AppUser user = appUserRepo.findByEmail("john.doe@example.com");
        assertFalse(user.isEmailVerified());

        ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService).sendConfirmationEmail(emailCaptor.capture(), tokenCaptor.capture());
        String confirmationToken = tokenCaptor.getValue();

        mockMvc.perform(get("/auth/confirm_email")
                        .param("token", confirmationToken))
                .andExpect(status().isOk());

        AppUser verifiedUser = appUserRepo.findByEmail("john.doe@example.com");
        assertTrue(verifiedUser.isEmailVerified());
    }

    @Test
    void registerUserWithExistingEmail_shouldThrowException() throws Exception {
        Map<String, String> registerRequest = new HashMap<>();
        registerRequest.put("firstName", "John");
        registerRequest.put("lastName", "Doe");
        registerRequest.put("email", "John.doe1@example.com");
        registerRequest.put("password", "Password123!");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().is4xxClientError());
    }
}
