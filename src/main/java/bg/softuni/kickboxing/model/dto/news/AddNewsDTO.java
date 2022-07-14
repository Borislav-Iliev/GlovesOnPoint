package bg.softuni.kickboxing.model.dto.news;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AddNewsDTO {

    @NotBlank(message = "Title must be provided!")
    @Size(min = 2, message = "Title length must be at least 2 characters!")
    private String title;

    @NotBlank(message = "Content must be provided!")
    @Size(min = 5, message = "Content length must be at least 5 characters!")
    private String content;

    @NotBlank(message = "ImageUrl must be provided!")
    private String imageUrl;

    public AddNewsDTO() {
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
}
