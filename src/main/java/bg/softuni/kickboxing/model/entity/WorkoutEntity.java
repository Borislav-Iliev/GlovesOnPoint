package bg.softuni.kickboxing.model.entity;

import bg.softuni.kickboxing.model.enums.WorkoutLevelEnum;
import bg.softuni.kickboxing.model.enums.WorkoutTypeEnum;

import javax.persistence.*;

@Entity
@Table(name = "workouts")
public class WorkoutEntity extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkoutLevelEnum level;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkoutTypeEnum type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String imageUrl;

    @ManyToOne
    private UserEntity author;

    public WorkoutEntity() {
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

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }
}
