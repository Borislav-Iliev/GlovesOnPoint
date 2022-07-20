package bg.softuni.kickboxing.model.dto.comment;

import java.time.LocalDateTime;

public class CommentDTO {

    private String content;
    private LocalDateTime createdOn;

    public CommentDTO() {
    }

    public CommentDTO(String content, LocalDateTime createdOn) {
        this.content = content;
        this.createdOn = createdOn;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
}
