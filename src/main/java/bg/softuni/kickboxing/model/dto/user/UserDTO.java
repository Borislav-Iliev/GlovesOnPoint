package bg.softuni.kickboxing.model.dto.user;

import bg.softuni.kickboxing.model.entity.UserRoleEntity;

import java.util.List;

public class UserDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private List<UserRoleEntity> userRoles;

    public UserDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageUrl() {
        if (this.imageUrl == null || this.imageUrl.isBlank()) {
            return "/images/home-page-background.jpg";
        }
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public List<UserRoleEntity> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRoleEntity> userRoles) {
        this.userRoles = userRoles;
    }
}
