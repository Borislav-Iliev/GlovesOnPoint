package bg.softuni.kickboxing.model.dto.post;

import bg.softuni.kickboxing.model.enums.PostCategoryEnum;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddPostDTO {

    @NotBlank(message = "Title must be provided!")
    @Size(min = 2, message = "Title length must be at least 2 characters!")
    private String title;

    @NotBlank(message = "ImageUrl must be provided!")
    @URL(message = "Must be a valid Url!")
    private String imageUrl;

    @NotBlank(message = "Content must be provided!")
    @Size(min = 5, message = "Content length must be at least 5 characters!")
    private String content;

    @NotNull(message = "Category must be selected!")
    private PostCategoryEnum category;

    public AddPostDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
}
