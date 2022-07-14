package bg.softuni.kickboxing.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

    @Column(nullable = false)
    private String content;

    @ManyToOne
    private PostEntity post;

    public CommentEntity() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }
}
