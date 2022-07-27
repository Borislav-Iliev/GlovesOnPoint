package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.model.entity.NewsEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.user.GlovesOnPointUserDetails;
import bg.softuni.kickboxing.repository.NewsRepository;
import bg.softuni.kickboxing.repository.UserRepository;
import bg.softuni.kickboxing.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NewsControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    private GlovesOnPointUserDetails userDetails;

    private UserEntity userEntity;

    private UserEntity moderatorEntity;

    private NewsEntity newsEntity;

    @BeforeEach
    public void setUp() {
        this.userEntity = initUser(this.userRoleRepository.findAll().get(2));
        this.moderatorEntity = initModerator(this.userRoleRepository.findAll().get(1));
        this.newsEntity = initNews();
        this.userDetails = (GlovesOnPointUserDetails) this.userDetailsService
                .loadUserByUsername(this.moderatorEntity.getUsername());
    }

    public UserEntity initModerator(UserRoleEntity moderatorRole) {
        UserEntity testUserEntity = new UserEntity()
                .setUsername("TestUsername")
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("test@example.com")
                .setPassword("testPassword")
                .setAge(20)
                .setImageUrl("image:/url")
                .setUserRoles(List.of(moderatorRole));
        testUserEntity.setId(1L);

        this.userRepository.save(testUserEntity);
        return testUserEntity;
    }

    public UserEntity initUser(UserRoleEntity userRole) {
        UserEntity testUserEntity = new UserEntity()
                .setUsername("User")
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("test2@example.com")
                .setPassword("testPassword")
                .setAge(20)
                .setImageUrl("image:/url")
                .setUserRoles(List.of(userRole));
        testUserEntity.setId(1L);

        this.userRepository.save(testUserEntity);
        return testUserEntity;
    }

    public NewsEntity initNews() {
        NewsEntity newsEntity = new NewsEntity()
                .setTitle("Title")
                .setContent("Some test content!")
                .setCreatedOn(LocalDate.now())
                .setViews(0)
                .setImageUrl("image:/url");
        newsEntity.setId(1L);

        this.newsRepository.save(newsEntity);
        return newsEntity;
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    public void testNewsPageShownForAuthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/news"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("news", "trending"))
                .andExpect(view().name("news"));
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    public void testNewsDetailsPageShownForAuthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/news/details/{id}", this.newsEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("news"))
                .andExpect(view().name("news-details"));
    }

    @Test
    @WithMockUser(username = "User", roles = "MODERATOR")
    public void testAddPostWithUser_ValidData() throws Exception {
        this.mockMvc.perform(post("/news/add")
                        .with(user(this.userDetails))
                        .param("title", "Valid Title!")
                        .param("imageUrl", "https://www.example.com/")
                        .param("content", "Valid content!!!")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/news"));
    }

    @Test
    @WithMockUser(username = "User", roles = "MODERATOR")
    public void testAddPostWithUser_InvalidData() throws Exception {
        this.mockMvc.perform(post("/news/add")
                        .with(user(this.userDetails))
                        .param("title", "Valid Title!")
                        .param("imageUrl", "https://www.example.com/")
                        .param("content", "")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("addNewsModel", "org.springframework.validation.BindingResult.addNewsModel"))
                .andExpect(redirectedUrl("/news/add"));
    }

    @Test
    @WithMockUser(username = "User", roles = "MODERATOR")
    public void testAddNewsPageShownForAuthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/news/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("news-add"));
    }

    @Test
    @WithMockUser(username = "User", roles = "MODERATOR")
    public void testDeleteNewsPageWithModeratorUser() throws Exception {
        this.mockMvc.perform(get("/news/delete/{id}", this.newsEntity.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/news"));
    }
}
