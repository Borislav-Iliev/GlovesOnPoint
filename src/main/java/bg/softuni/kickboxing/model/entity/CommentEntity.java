package bg.softuni.kickboxing.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

    @Column(nullable = false)
    private String content;

    @Column(name = "is_approved", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
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

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }
}
