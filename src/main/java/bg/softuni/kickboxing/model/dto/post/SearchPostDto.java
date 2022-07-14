package bg.softuni.kickboxing.model.dto.post;

import javax.validation.constraints.NotBlank;

public class SearchPostDto {

    @NotBlank(message = "Search bar cannot be empty!")
    private String query;

    public SearchPostDto() {
    }

    public String getQuery() {
        return query;
    }
}
