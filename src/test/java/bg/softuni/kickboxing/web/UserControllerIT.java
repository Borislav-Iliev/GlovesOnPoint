package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.model.entity.CommentEntity;
import bg.softuni.kickboxing.model.entity.PostEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.PostCategoryEnum;
import bg.softuni.kickboxing.model.enums.UserRoleEnum;
import bg.softuni.kickboxing.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Test
    public void testRegistrationPageShown() throws Exception {
        this.mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth-register"));
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
                .andExpect(redirectedUrl("/users/register"));
    }

    @Test
    public void testLoginPageShown() throws Exception {
        this.mockMvc.perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth-login"));
    }
}
