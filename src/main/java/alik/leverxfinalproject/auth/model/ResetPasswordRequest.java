package alik.leverxfinalproject.auth.model;

import jakarta.validation.constraints.Pattern;

public class ResetPasswordRequest {
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()-+=.])(?=\\S+$).{8,20}$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, one special character, and no whitespace. Minimum length is 8 and maximum is 20.")
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }
}
