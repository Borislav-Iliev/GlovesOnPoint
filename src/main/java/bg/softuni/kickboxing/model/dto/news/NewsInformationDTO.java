package bg.softuni.kickboxing.model.dto.news;

import java.time.LocalDate;

public class NewsInformationDTO {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDate date;

    public NewsInformationDTO() {
    }

    public NewsInformationDTO(Long id, String title, String content, String imageUrl, LocalDate date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public LocalDate getDate() {
        return date;
    }
}
