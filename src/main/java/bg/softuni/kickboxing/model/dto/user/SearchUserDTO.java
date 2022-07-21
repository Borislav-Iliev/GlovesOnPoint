package bg.softuni.kickboxing.model.dto.user;

import bg.softuni.kickboxing.model.validation.InvalidUsername;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class SearchUserDTO {
    @NotEmpty(message = "Please enter a username!")
    @Size(min = 2, max = 20, message = "Username length must be between 2 and 20 characters!")
    @InvalidUsername(message = "User with this username does not exist!")
    private String query;

    public SearchUserDTO() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
