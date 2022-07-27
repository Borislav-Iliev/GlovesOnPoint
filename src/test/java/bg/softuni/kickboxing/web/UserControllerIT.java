package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.user.GlovesOnPointUserDetails;
import bg.softuni.kickboxing.repository.UserRepository;
import bg.softuni.kickboxing.repository.UserRoleRepository;
import bg.softuni.kickboxing.service.EmailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @MockBean
    private EmailService mockEmailService;

    private UserEntity userEntity;

    private GlovesOnPointUserDetails userDetails;

    @BeforeEach
    public void setUp() {
        this.userEntity = initUser(this.userRoleRepository.findAll());
        this.userDetails = (GlovesOnPointUserDetails)
                this.userDetailsService.loadUserByUsername("TestUsername");
    }

    @AfterEach
    public void tearDown() {
        this.userRepository.deleteAll();
        this.userRoleRepository.deleteAll();
    }

    public UserEntity initUser(List<UserRoleEntity> userRoles) {
        UserEntity testUserEntity = new UserEntity()
                .setUsername("TestUsername")
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("test@example.com")
                .setPassword("testPassword")
                .setAge(20)
                .setImageUrl("image:/url")
                .setUserRoles(userRoles);
        testUserEntity.setId(1L);

        this.userRepository.save(testUserEntity);
        return testUserEntity;
    }

    @Test
    public void testRegistrationPageShown() throws Exception {
        this.mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth-register"));
    }

    @Test
    public void testUserRegistration() throws Exception {
        this.mockMvc.perform(post("/users/register")
                        .param("firstName", "Test")
                        .param("lastName", "Testov")
                        .param("username", "TestUsername1")
                        .param("email", "email@example.com")
                        .param("age", "20")
                        .param("imageUrl", "image:/url")
                        .param("password", "testPassword")
                        .param("confirmPassword", "testPassword")
                        .with(csrf()))
                .andExpect(redirectedUrl("/"));

        verify(this.mockEmailService)
                .sendRegistrationEmail("email@example.com", "Test Testov");
    }

    @Test
    public void testUserRegistration_InvalidUserData() throws Exception {
        this.mockMvc.perform(post("/users/register")
                        .param("firstName", "T")
                        .param("lastName", "Testov")
                        .param("username", "TestUsername")
                        .param("email", "email@example.com")
                        .param("age", "20")
                        .param("imageUrl", "image:/url")
                        .param("password", "testPassword")
                        .param("confirmPassword", "testPassword")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("userRegistrationModel", "org.springframework.validation.BindingResult.userRegistrationModel"))
                .andExpect(redirectedUrl("/users/register"));
    }

    @Test
    public void testLoginPageShown() throws Exception {
        this.mockMvc.perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth-login"));
    }

    @Test
    @WithAnonymousUser
    public void testProfilePageShouldRedirectToLoginPageForUnauthorizedUser() throws Exception {
        this.mockMvc.perform(get("/users/profile/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithAnonymousUser
    public void testEditProfilePageShouldRedirectToLoginPageForUnauthorizedUser() throws Exception {
        this.mockMvc.perform(get("/users/profile/edit/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    public void profilePageOpenFromUnauthenticatedUserShouldRedirectToLoginPage() throws Exception {
        this.mockMvc
                .perform(get("/users/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = {"ADMIN", "MODERATOR", "USERNAME"})
    public void profilePageOpenFromAuthenticatedUser() throws Exception {
        this.mockMvc
                .perform(get("/users/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("profile"));
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = {"ADMIN", "MODERATOR", "USERNAME"})
    public void editProfilePageOpenFromAuthenticatedUser() throws Exception {
        this.mockMvc
                .perform(get("/users/profile/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("profile-edit"));
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = {"ADMIN", "MODERATOR", "USERNAME"})
    public void testEditProfilePage_InvalidData() throws Exception {
        this.mockMvc
                .perform(post("/users/profile/edit")
                        .param("username", "TestUsername12")
                        .param("email", "example2@example.com")
                        .param("firstName", "TestFirst")
                        .param("lastName", "TestLast")
                        .param("imageUrl", "image:/https")
                        .param("password", "TestPassword")
                        .param("confirmPassword", "TestPassword")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("editUserModel", "org.springframework.validation.BindingResult.editUserModel"))
                .andExpect(redirectedUrl("/users/profile/edit"));
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = {"ADMIN", "MODERATOR", "USERNAME"})
    public void testEditProfilePage_ValidData() throws Exception {
        this.mockMvc
                .perform(post("/users/profile/edit")
                        .param("username", "TestUsername12")
                        .param("email", "example2@example.com")
                        .param("firstName", "TestFirst")
                        .param("lastName", "TestLast")
                        .param("imageUrl", "https://www.example.com")
                        .param("password", "TestPassword")
                        .param("confirmPassword", "TestPassword")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("success"))
                .andExpect(redirectedUrl("/users/profile/edit"));
    }

    @Test
    @WithMockUser(username = "TestUsername", password = "testPassword", roles = {"ADMIN", "MODERATOR", "USERNAME"})
    public void testLoginErrorPageShown() throws Exception {
        this.mockMvc
                .perform(post("/users/login-error")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("badCredentials", "username"))
                .andExpect(redirectedUrl("/users/login"));
    }
}
