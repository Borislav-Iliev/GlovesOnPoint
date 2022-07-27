package bg.softuni.kickboxing.model.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "news")
public class NewsEntity extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    @Column(name = "created_on", nullable = false)
    private LocalDate createdOn;

    public int views;

    @Lob
    private String imageUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity author;

    public NewsEntity() {
    }

    public String getTitle() {
        return title;
    }

    public NewsEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public NewsEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public NewsEntity setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public int getViews() {
        return views;
    }

    public NewsEntity setViews(int views) {
        this.views = views;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public NewsEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public NewsEntity setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }
}
