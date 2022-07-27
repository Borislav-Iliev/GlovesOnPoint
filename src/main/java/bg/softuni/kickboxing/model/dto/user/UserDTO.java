package bg.softuni.kickboxing.model.dto.user;

import bg.softuni.kickboxing.model.entity.CommentEntity;
import bg.softuni.kickboxing.model.entity.PostEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;

import java.util.List;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private List<UserRoleEntity> userRoles;
    private List<PostEntity> posts;
    private List<CommentEntity> comments;

    public UserDTO() {
    }

    public Long getId() {
        return id;
    }

    public UserDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getImageUrl() {
        if (this.imageUrl == null || this.imageUrl.isBlank()) {
            return "/images/home-page-background.jpg";
        }
        return imageUrl;
    }

    public UserDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public List<UserRoleEntity> getUserRoles() {
        return userRoles;
    }

    public UserDTO setUserRoles(List<UserRoleEntity> userRoles) {
        this.userRoles = userRoles;
        return this;
    }

    public String getRole() {
        return this.userRoles.toArray()[this.userRoles.size() - 1].toString();
    }

    public List<PostEntity> getPosts() {
        return posts;
    }

    public UserDTO setPosts(List<PostEntity> posts) {
        this.posts = posts;
        return this;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public UserDTO setComments(List<CommentEntity> comments) {
        this.comments = comments;
        return this;
    }
}
