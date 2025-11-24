package alik.leverxfinalproject.model.request;

import jakarta.validation.constraints.*;

public class UserCommentRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @Null
    private String email;
    @NotBlank
    private String comment;
    @Min(1)
    @Max(5)
    private int rating;


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }
}
