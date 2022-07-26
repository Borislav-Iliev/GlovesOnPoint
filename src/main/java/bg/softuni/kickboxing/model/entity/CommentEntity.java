package bg.softuni.kickboxing.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

    @Column(nullable = false)
    private String content;

    @Column(name = "is_approved", nullable = false)
    private boolean isApproved;

    private LocalDateTime createdOn;

    @ManyToOne
    private PostEntity post;

    @ManyToOne
    private UserEntity author;

    public CommentEntity() {
    }

    public String getContent() {
        return content;
    }

    public CommentEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public CommentEntity setApproved(boolean approved) {
        isApproved = approved;
        return this;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public CommentEntity setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public PostEntity getPost() {
        return post;
    }

    public CommentEntity setPost(PostEntity post) {
        this.post = post;
        return this;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public CommentEntity setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }
}
