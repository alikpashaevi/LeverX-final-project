package alik.leverxfinalproject.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//@Data
public class LoginRequest {

    @Email
    private String email;
    @NotBlank
    private String password;



    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
