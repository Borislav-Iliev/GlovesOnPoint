package bg.softuni.kickboxing.model.dto.news;

import java.time.LocalDate;

public class NewsDTO {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private int views;
    private LocalDate createdOn;

    public NewsDTO() {
    }

    public NewsDTO(Long id, String title, String content, String imageUrl, int views, LocalDate createdOn) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.views = views;
        this.createdOn = createdOn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public String getSummary() {
        return this.content.substring(0, 14) + "...";
    }
}
