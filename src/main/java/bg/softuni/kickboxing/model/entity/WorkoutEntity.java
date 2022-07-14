package bg.softuni.kickboxing.model.entity;

import bg.softuni.kickboxing.model.enums.WorkoutLevelEnum;

import javax.persistence.*;

@Entity
@Table(name = "workouts")
public class WorkoutEntity extends BaseEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkoutLevelEnum level;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String imageUrl;

    @ManyToOne
    private UserEntity author;

    public WorkoutEntity() {
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

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }
}
