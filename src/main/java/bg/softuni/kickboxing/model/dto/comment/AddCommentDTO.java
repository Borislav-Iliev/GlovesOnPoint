package bg.softuni.kickboxing.model.dto.comment;

import javax.validation.constraints.NotBlank;

public class AddCommentDTO {

    @NotBlank(message = "Comment must be provided!")
    private String content;

    public AddCommentDTO() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
