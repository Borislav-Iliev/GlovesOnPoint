package bg.softuni.kickboxing.model.dto.post;

import bg.softuni.kickboxing.model.enums.PostCategoryEnum;

import java.time.LocalDateTime;

public class PostInformationDTO {
    private Long authorId;
    private String authorUsername;
    private Long id;
    private String title;
    private PostCategoryEnum category;
    private int views;
    private LocalDateTime createdOn;

    public PostInformationDTO() {
    }

    public PostInformationDTO(Long authorId, String authorUsername, Long id, String title, PostCategoryEnum category, int views, LocalDateTime createdOn) {
        this.authorId = authorId;
        this.authorUsername = authorUsername;
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
}
