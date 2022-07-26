package bg.softuni.kickboxing.model.entity;

import bg.softuni.kickboxing.model.enums.PostCategoryEnum;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

    @Column(columnDefinition = "INTEGER DEFAULT 0", nullable = false)
    private int views;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "is_approved", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isApproved;

    @OneToMany(mappedBy = "post")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<CommentEntity> comments;

    @ManyToOne
    private UserEntity author;

    public PostEntity() {
        this.comments = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public PostEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public PostEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public PostCategoryEnum getCategory() {
        return category;
    }

    public PostEntity setCategory(PostCategoryEnum category) {
        this.category = category;
        return this;
    }

    public int getViews() {
        return views;
    }

    public PostEntity setViews(int views) {
        this.views = views;
        return this;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public PostEntity setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public PostEntity setApproved(boolean approved) {
        isApproved = approved;
        return this;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public PostEntity setComments(List<CommentEntity> comments) {
        this.comments = comments;
        return this;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public PostEntity setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }
}
