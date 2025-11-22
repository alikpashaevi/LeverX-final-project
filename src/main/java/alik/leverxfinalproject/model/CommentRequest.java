package alik.leverxfinalproject.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class CommentRequest {

    @NotBlank
    private String text;
    @Min(1)
    @Max(5)
    private int rating;

    public String getText() {
        return text;
    }

    public int getRating() {
        return rating;
    }

}
