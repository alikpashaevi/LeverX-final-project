package alik.leverxfinalproject.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class CommentRequest {

    @NotBlank
    private String text;
    @Positive
    private Long authorId;

    public String getText() {
        return text;
    }


    public Long getAuthorId() {
        return authorId;
    }

}
