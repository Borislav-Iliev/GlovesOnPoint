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
public class UserRegistrationDTO {

    @NotBlank(message = "Username must be provided!")
    @Size(min = 2, max = 20, message = "Username length must be between 2 and 20 characters!")
    @UniqueUsername(message = "Username has already been taken!")
    private String username;

    @NotBlank(message = "First name must be provided!")
    @Size(min = 2, max = 20, message = "First name length must be between 2 and 20 characters!")
    private String firstName;

    @NotBlank(message = "Last name must be provided!")
    @Size(min = 2, max = 20, message = "Last name length must be between 2 and 20 characters!")
    private String lastName;

    @NotBlank(message = "Email must be provided!")
    @Email(message = "Must be valid email!")
    @UniqueEmail(message = "Email has already been taken!")
    private String email;

    @NotNull(message = "Age must be provided!")
    @Positive(message = "Age must be positive!")
    private int age;

    private String imageUrl;

    @NotBlank(message = "Password must be provided!")
    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 characters!")
    private String password;

    @NotBlank(message = "Password must be provided!")
    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 characters!")
    private String confirmPassword;

    public UserRegistrationDTO() {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
