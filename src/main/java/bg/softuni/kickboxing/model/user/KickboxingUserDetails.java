package bg.softuni.kickboxing.model.user;

import bg.softuni.kickboxing.model.entity.CommentEntity;
import bg.softuni.kickboxing.model.entity.PostEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class KickboxingUserDetails implements UserDetails {

    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String imageUrl;
    private final List<PostEntity> posts;
    private final List<CommentEntity> comments;
    private final Collection<GrantedAuthority> authorities;

    public KickboxingUserDetails(String username, String firstName, String lastName, String email, String password, String imageUrl, List<PostEntity> posts, List<CommentEntity> comments, Collection<GrantedAuthority> authorities) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
        this.posts = posts;
        this.comments = comments;
        this.authorities = authorities;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        if (this.imageUrl == null) {
            return "/images/home-page-background.jpg";
        }
        return imageUrl;
    }

    public List<PostEntity> getPosts() {
        return posts;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (getFirstName() != null) {
            fullName.append(getFirstName());
        }
        if (getLastName() != null) {
            if (!fullName.isEmpty()) {
                fullName.append(" ");
            }
            fullName.append(getLastName());
        }

        return fullName.toString();
    }

    public String getRole() {
        return this.authorities.toArray()[2].toString().replace("ROLE_", "");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
