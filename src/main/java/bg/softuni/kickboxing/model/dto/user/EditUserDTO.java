package bg.softuni.kickboxing.model.dto.user;

import bg.softuni.kickboxing.model.validation.FieldMatch;
import bg.softuni.kickboxing.model.validation.UniqueEmail;
import bg.softuni.kickboxing.model.validation.UniqueUsername;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

@FieldMatch(
        first = "password",
        second = "confirmPassword",
        message = "Passwords must match!"
)
public class EditUserDTO {
    @UniqueUsername(message = "Username has already been taken!")
    private String username;

    private String firstName;

    private String lastName;

    @Email(message = "Must be valid email!")
    @UniqueEmail(message = "Email has already been taken!")
    private String email;

    @URL(message = "must be valid url!")
    private String imageUrl;

    private String password;

    private String confirmPassword;

    public EditUserDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
