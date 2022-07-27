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

    @Lob
    private String content;

    @Lob
    private String imageUrl;

    @ManyToOne
    private UserEntity author;

    public WorkoutEntity() {
    }

    public String getTitle() {
        return title;
    }

    public WorkoutEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public WorkoutLevelEnum getLevel() {
        return level;
    }

    public WorkoutEntity setLevel(WorkoutLevelEnum level) {
        this.level = level;
        return this;
    }

    public WorkoutTypeEnum getType() {
        return type;
    }

    public WorkoutEntity setType(WorkoutTypeEnum type) {
        this.type = type;
        return this;
    }

    public String getContent() {
        return content;
    }

    public WorkoutEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public WorkoutEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public WorkoutEntity setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }
}
