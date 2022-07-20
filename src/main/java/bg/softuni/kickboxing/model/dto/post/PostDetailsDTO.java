package bg.softuni.kickboxing.model.dto.post;

import bg.softuni.kickboxing.model.dto.comment.CommentDTO;
import bg.softuni.kickboxing.model.enums.PostCategoryEnum;

import java.time.LocalDateTime;
import java.util.List;

public class PostDetailsDTO {
    private Long id;
    private String title;
    private String content;
    private PostCategoryEnum category;
    private int views;
    private LocalDateTime createdOn;
    private List<CommentDTO> comments;
    private String authorUsername;
    private String authorImageUrl;

    public PostDetailsDTO() {
    }

    public PostDetailsDTO(Long id, String title, String content, PostCategoryEnum category, int views, LocalDateTime createdOn, List<CommentDTO> comments, String authorUsername, String authorImageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.views = views;
        this.createdOn = createdOn;
        this.comments = comments;
        this.authorUsername = authorUsername;
        this.authorImageUrl = authorImageUrl;
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

    public String getContent() {
        return content;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public String getAuthorImageUrl() {
        if (this.authorImageUrl == null || this.authorImageUrl.isBlank()) {
            return "/images/home-page-background.jpg";
        }
        return authorImageUrl;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }
}
