package alik.leverxfinalproject.model;

import java.time.LocalDateTime;

public class UserDTO {
    private long id;
    private String FirstName;
    private String LastName;
    private String email;
    private Double avgRating;
    private Long ratingCount;

    public UserDTO(long id, String firstName, String lastName, String email, Double avgRating, Long ratingCount) {
        this.id = id;
        FirstName = firstName;
        LastName = lastName;
        this.email = email;
        this.avgRating = avgRating;
        this.ratingCount = ratingCount;
    }

    public UserDTO() {

    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getEmail() {
        return email;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public Long getRatingCount() {
        return ratingCount;
    }

}
