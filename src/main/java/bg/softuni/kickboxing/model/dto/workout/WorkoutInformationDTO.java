package bg.softuni.kickboxing.model.dto.workout;

import bg.softuni.kickboxing.model.enums.WorkoutLevelEnum;

public class WorkoutInformationDTO {
    private Long id;
    private WorkoutLevelEnum level;
    private String content;
    private String imageUrl;

    public WorkoutInformationDTO() {
    }

    public WorkoutInformationDTO(Long id, WorkoutLevelEnum level, String content, String imageUrl) {
        this.id = id;
        this.level = level;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
