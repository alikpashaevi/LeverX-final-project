package alik.leverxfinalproject.model;

public class GameObjectDTO {
    private Long id;
    private String title;
    private String text;
    private Long gameId;
    private Long userId;


    public Long getId() {
        return id;
    }

    public String getName() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Long getGameId() {
        return gameId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
