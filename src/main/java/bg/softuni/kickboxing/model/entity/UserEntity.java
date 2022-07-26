package bg.softuni.kickboxing.model.entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int age;

    @Column(name = "imageUrl")
    private String imageUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<UserRoleEntity> userRoles;

    @OneToMany(mappedBy = "author", targetEntity = PostEntity.class)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<PostEntity> posts;

    @OneToMany(mappedBy = "author", targetEntity = CommentEntity.class)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<CommentEntity> comments;

    public UserEntity() {
        this.userRoles = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getAge() {
        return age;
    }

    public UserEntity setAge(int age) {
        this.age = age;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public UserEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public List<UserRoleEntity> getUserRoles() {
        return userRoles;
    }

    public UserEntity setUserRoles(List<UserRoleEntity> userRoles) {
        this.userRoles = userRoles;
        return this;
    }

    public List<PostEntity> getPosts() {
        return posts;
    }

    public UserEntity setPosts(List<PostEntity> posts) {
        this.posts = posts;
        return this;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public UserEntity setComments(List<CommentEntity> comments) {
        this.comments = comments;
        return this;
    }

    public void addRole(UserRoleEntity userRole) {
        this.userRoles.add(userRole);
    }

    public void removeModeratorRole() {
        this.userRoles.remove(1);
    }
}
