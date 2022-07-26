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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate date) {
        this.createdOn = date;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }
}
