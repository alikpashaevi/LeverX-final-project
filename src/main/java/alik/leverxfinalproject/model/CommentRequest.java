package alik.leverxfinalproject.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class CommentRequest {

    @NotBlank
    private String text;

    public String getText() {
        return text;
    }

}
