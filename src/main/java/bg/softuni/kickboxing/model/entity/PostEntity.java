package bg.softuni.kickboxing.model.entity;

import bg.softuni.kickboxing.model.enums.PostCategoryEnum;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class PostEntity extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostCategoryEnum category;

    @Column(nullable = false)
    private String imageUrl;

    @Column(columnDefinition = "INTEGER DEFAULT 0", nullable = false)
    private int views;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "is_approved", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isApproved;

    @OneToMany(mappedBy = "post")
    private List<CommentEntity> comments;

    @ManyToOne
    private UserEntity author;

    public PostEntity() {
        this.comments = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostCategoryEnum getCategory() {
        return category;
    }

    public void setCategory(PostCategoryEnum category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }
}
