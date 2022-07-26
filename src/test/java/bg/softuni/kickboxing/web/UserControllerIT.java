package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.model.dto.user.UserDTO;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.UserRoleEnum;
import bg.softuni.kickboxing.repository.UserRepository;
import bg.softuni.kickboxing.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private EmailService mockEmailService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
        initUser();
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
                        .param("username", "TestUsername")
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
}