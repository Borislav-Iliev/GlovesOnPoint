package bg.softuni.kickboxing.model.dto.post;

import bg.softuni.kickboxing.model.enums.PostCategoryEnum;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PostDTO {
    private Long authorId;
    private String authorUsername;
    private String authorImageUrl;
    private Long id;
    private String title;
    private PostCategoryEnum category;
    private int views;
    private LocalDateTime createdOn;

    public PostDTO() {
    }

    public PostDTO(Long authorId, String authorUsername, String authorImageUrl, Long id, String title, PostCategoryEnum category, int views, LocalDateTime createdOn) {
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.authorImageUrl = authorImageUrl;
        this.id = id;
        this.title = title;
        this.category = category;
        this.views = views;
        this.createdOn = createdOn;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public PostCategoryEnum getCategory() {
        return category;
    }

    public int getViews() {
        return views;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public String getAuthorImageUrl() {
        if (this.authorImageUrl == null || this.authorImageUrl.isBlank()) {
            return "/images/home-page-background.jpg";
        }
        return authorImageUrl;
    }

    public String formatCreatedOn() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return this.createdOn.format(formatter);
    }
}
