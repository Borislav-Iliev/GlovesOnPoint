package bg.softuni.kickboxing.model.dto.comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentDTO {

    private Long id;
    private String content;
    private LocalDateTime createdOn;
    private String authorUsername;
    private String authorImageUrl;
    private boolean isApproved;

    public CommentDTO() {
    }

    public CommentDTO(Long id, String content, LocalDateTime createdOn, String authorUsername, String authorImageUrl) {
        this.id = id;
        this.content = content;
        this.createdOn = createdOn;
        this.authorUsername = authorUsername;
        this.authorImageUrl = authorImageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getAuthorImageUrl() {
        if (this.authorImageUrl == null || this.authorImageUrl.isBlank()) {
            return "/images/home-page-background.jpg";
        }
        return authorImageUrl;
    }

    public void setAuthorImageUrl(String authorImageUrl) {
        this.authorImageUrl = authorImageUrl;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public String formatCreatedOn() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return this.createdOn.format(formatter);
    }
}
