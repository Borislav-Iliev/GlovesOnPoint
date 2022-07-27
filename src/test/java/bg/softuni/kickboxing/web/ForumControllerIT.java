package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.model.entity.PostEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.PostCategoryEnum;
import bg.softuni.kickboxing.model.user.GlovesOnPointUserDetails;
import bg.softuni.kickboxing.repository.PostRepository;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ForumControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    private GlovesOnPointUserDetails userDetails;

    private UserEntity userEntity;

    private PostEntity postEntity;

    @BeforeEach
    public void setUp() {
        this.userEntity = initUser(this.userRoleRepository.findAll().get(2));
        this.postEntity = initPost();
        this.userDetails = (GlovesOnPointUserDetails) this.userDetailsService
                .loadUserByUsername(this.userEntity.getUsername());
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

    private PostEntity initPost() {
        PostEntity postEntity = new PostEntity()
                .setTitle("Title")
                .setContent("Content")
                .setCategory(PostCategoryEnum.PROBLEM)
                .setViews(0)
                .setCreatedOn(LocalDateTime.now())
                .setApproved(false);
        this.postRepository.save(postEntity);
        return postEntity;
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    public void testForumPageShownForAuthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/forum"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("posts"))
                .andExpect(view().name("forum"));
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    public void testAddPostPageShownForAuthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/forum/posts/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("post-add"));
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    public void testAddPostWithUser_ValidData() throws Exception {
        this.mockMvc.perform(post("/forum/posts/add")
                .with(user(this.userDetails))
                        .param("title", "Valid Title!")
                        .param("category", "PROBLEM")
                        .param("content", "Valid content!")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("success"))
                .andExpect(redirectedUrl("/forum/posts/add"));
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    public void testAddPostWithUser_InvalidData() throws Exception {
        this.mockMvc.perform(post("/forum/posts/add")
                        .with(user(this.userDetails))
                        .param("title", "")
                        .param("category", "PROBLEM")
                        .param("content", "Valid content!")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("addPostModel", "org.springframework.validation.BindingResult.addPostModel"))
                .andExpect(redirectedUrl("/forum/posts/add"));
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    public void testPostDetailsPageShownForAuthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/forum/posts/{id}", this.postEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(view().name("post-details"));
    }
}
