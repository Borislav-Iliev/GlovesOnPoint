package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.model.entity.CommentEntity;
import bg.softuni.kickboxing.model.entity.PostEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.PostCategoryEnum;
import bg.softuni.kickboxing.model.user.GlovesOnPointUserDetails;
import bg.softuni.kickboxing.repository.CommentRepository;
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
public class CommentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    private GlovesOnPointUserDetails userDetails;

    private UserEntity userEntity;

    private PostEntity postEntity;

    private CommentEntity commentEntity;

    @BeforeEach
    public void setUp() {
        this.userEntity = initUser(this.userRoleRepository.findAll().get(2));
        this.postEntity = initPost();
        this.commentEntity = initComment();
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

    private CommentEntity initComment() {
        CommentEntity commentEntity = new CommentEntity()
                .setContent("Content")
                .setApproved(false)
                .setCreatedOn(LocalDateTime.now())
                .setAuthor(this.userEntity)
                .setPost(this.postEntity);
        commentEntity.setId(1L);
        this.commentRepository.save(commentEntity);
        return commentEntity;
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    public void testAddCommentPageShown() throws Exception {
        this.mockMvc.perform(get("/forum/posts/1/add/comment"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(view().name("comment-add"));
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    public void testAddCommentPage_ValidData() throws Exception {
        this.mockMvc.perform(post("/forum/posts/1/add/comment")
                        .param("content", "Valid content!")
                        .with(user(this.userDetails))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("success"))
                .andExpect(redirectedUrl("/forum/posts/1/add/comment"));
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    public void testAddCommentPage_InvalidData() throws Exception {
        this.mockMvc.perform(post("/forum/posts/1/add/comment")
                        .param("content", "")
                        .with(user(this.userDetails))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("addCommentModel", "org.springframework.validation.BindingResult.addCommentModel"))
                .andExpect(redirectedUrl("/forum/posts/1/add/comment"));
    }
}
