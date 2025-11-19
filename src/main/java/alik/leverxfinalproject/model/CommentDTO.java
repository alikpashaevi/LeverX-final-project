package alik.leverxfinalproject.model;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private String text;
    private Long userId;
    private String authorId;
    private LocalDateTime createdAt;

    public CommentDTO(Long id, String text, Long userId, String authorId, LocalDateTime createdAt) {
        this.id = id;
        this.text = text;
        this.userId = userId;
        this.authorId = authorId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Long getUserId() {
        return userId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
