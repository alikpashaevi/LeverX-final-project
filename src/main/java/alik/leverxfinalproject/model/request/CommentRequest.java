package alik.leverxfinalproject.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class CommentRequest {

    private String text;
    @Min(1)
    @Max(5)
    private int rating;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
