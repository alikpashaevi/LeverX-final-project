package alik.leverxfinalproject.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class GameObjectRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String text;
    @Positive
    private Long gameId;

    public String getName() {
        return name;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getText() {
        return text;
    }

}
