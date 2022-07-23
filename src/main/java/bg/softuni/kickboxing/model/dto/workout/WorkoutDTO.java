package bg.softuni.kickboxing.model.dto.workout;

import bg.softuni.kickboxing.model.enums.WorkoutLevelEnum;
import bg.softuni.kickboxing.model.enums.WorkoutTypeEnum;

public class WorkoutDTO {
    private Long id;
    private String title;
    private WorkoutLevelEnum level;
    private WorkoutTypeEnum type;
    private String content;
    private String imageUrl;

    public WorkoutDTO() {
    }

    public WorkoutDTO(Long id, String title, WorkoutLevelEnum level, WorkoutTypeEnum type, String content, String imageUrl) {
        this.id = id;
        this.title = title;
        this.level = level;
        this.type = type;
        this.content = content;
        this.imageUrl = imageUrl;
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

    public WorkoutLevelEnum getLevel() {
        return level;
    }

    public void setLevel(WorkoutLevelEnum level) {
        this.level = level;
    }

    public WorkoutTypeEnum getType() {
        return type;
    }

    public void setType(WorkoutTypeEnum type) {
        this.type = type;
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
