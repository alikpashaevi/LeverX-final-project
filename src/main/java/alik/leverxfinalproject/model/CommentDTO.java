package alik.leverxfinalproject.model;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private String text;
    private int rating;
    private Long userId;
    private String authorId;
    private LocalDateTime createdAt;
    private boolean isApproved;

    public CommentDTO(Long id, String text, int rating, Long userId, String authorId, LocalDateTime createdAt, boolean isApproved) {
        this.id = id;
        this.text = text;
        this.rating = rating;
        this.userId = userId;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.isApproved = isApproved;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getRating() {
        return rating;
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

    public boolean isApproved() {
        return isApproved;
    }
}
