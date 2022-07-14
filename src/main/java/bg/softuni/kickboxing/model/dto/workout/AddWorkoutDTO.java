package bg.softuni.kickboxing.model.dto.workout;

import bg.softuni.kickboxing.model.enums.WorkoutLevelEnum;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddWorkoutDTO {

    @NotNull(message = "WorkoutLevel must be selected!")
    private WorkoutLevelEnum level;

    @NotBlank(message = "Content must be provided!")
    @Size(min = 5, message = "Content length must be at least 5 characters!")
    private String content;

    @NotBlank(message = "ImageUrl must be provided!")
    @URL(message = "Must be a valid Url!")
    private String imageUrl;

    public AddWorkoutDTO() {
    }

    public WorkoutLevelEnum getLevel() {
        return level;
    }

    public void setLevel(WorkoutLevelEnum level) {
        this.level = level;
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
