package alik.leverxfinalproject.model;

public class GameObjectDTO {
    private Long id;
    private String title;
    private String text;
    private Long gameId;
    private Long userId;

    public GameObjectDTO(Long id, String title, String text, Long gameId, Long userId) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.gameId = gameId;
        this.userId = userId;
    }

    public GameObjectDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
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

    public void setTitle(String title) {
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
