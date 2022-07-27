package bg.softuni.kickboxing.web;


import bg.softuni.kickboxing.model.entity.CommentEntity;
import bg.softuni.kickboxing.model.entity.PostEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.PostCategoryEnum;
import bg.softuni.kickboxing.repository.CommentRepository;
import bg.softuni.kickboxing.repository.PostRepository;
import bg.softuni.kickboxing.repository.UserRepository;
import bg.softuni.kickboxing.repository.UserRoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ModeratorControllerIT {

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

    private UserEntity moderatorEntity;

    private UserEntity userEntity;

    private PostEntity postEntity;

    private CommentEntity commentEntity;

    @BeforeEach
    public void setUp() {
        this.moderatorEntity = initModerator(this.userRoleRepository.findAll().get(1));
        this.userEntity = initUser(this.userRoleRepository.findAll().get(0));
        this.postEntity = initPost();
        this.commentEntity = initComment();
    }

    @AfterEach
    public void tearDown() {
        this.userRepository.deleteAll();
        this.postRepository.deleteAll();
        this.commentRepository.deleteAll();
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
                .setUsername("TestUser")
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("test2@example.com")
                .setPassword("testPassword")
                .setAge(20)
                .setImageUrl("image:/url")
                .setUserRoles(List.of(userRole));
        testUserEntity.setId(2L);

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
                .setCreatedOn(LocalDateTime.now());
        commentEntity.setId(1L);
        this.commentRepository.save(commentEntity);
        return commentEntity;
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = "MODERATOR")
    public void testModeratorPageShownForModeratorUser() throws Exception {
        this.mockMvc.perform(get("/moderator"))
                .andExpect(status().isOk())
                .andExpect(view().name("moderator"));
    }

    @Test
    @WithMockUser(username = "TestUser", roles = "USER")
    public void testModeratorPageNotShownForNormalUser() throws Exception {
        this.mockMvc.perform(get("/moderator"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = "MODERATOR")
    public void testModeratorPostsPageShownForModeratorUser() throws Exception {
        this.mockMvc.perform(get("/moderator/posts"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("posts"))
                .andExpect(view().name("moderator-posts"));
    }

    @Test
    @WithMockUser(username = "TestUser", roles = "USER")
    public void testModeratorPostsPageNotShownForNormalUser() throws Exception {
        this.mockMvc.perform(get("/moderator/posts"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = "MODERATOR")
    public void testModeratorApprovePostUpdatePost() throws Exception {
        this.mockMvc.perform(get("/moderator/posts/approve/{id}", this.postEntity.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/moderator/posts"));

        Optional<PostEntity> optPost = this.postRepository
                .findById(this.postEntity.getId());
        boolean approved = optPost.get().isApproved();

        Assertions.assertTrue(approved);
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = "MODERATOR")
    public void testModeratorDisapprovePostShouldRedirectToPosts() throws Exception {
        this.mockMvc.perform(get("/moderator/posts/disapprove/{id}", this.postEntity.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/moderator/posts"));

    }

    @Test
    @WithMockUser(username = "TestUsername", roles = "MODERATOR")
    public void testModeratorCommentsPageShownForModeratorUser() throws Exception {
        this.mockMvc.perform(get("/moderator/comments"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("comments"))
                .andExpect(view().name("moderator-comments"));
    }

    @Test
    @WithMockUser(username = "TestUser", roles = "USER")
    public void testModeratorCommentsPageNotShownForNormalUser() throws Exception {
        this.mockMvc.perform(get("/moderator/comments"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = "MODERATOR")
    public void testModeratorApproveCommentUpdateComment() throws Exception {
        this.mockMvc.perform(get("/moderator/comments/approve/{id}", this.commentEntity.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/moderator/comments"));

        Optional<CommentEntity> optComment = this.commentRepository
                .findById(this.commentEntity.getId());
        boolean approved = optComment.get().isApproved();

        Assertions.assertTrue(approved);
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = "MODERATOR")
    public void testModeratorDisapproveCommentShouldRedirectToComments() throws Exception {
        this.mockMvc.perform(get("/moderator/comments/disapprove/{id}", this.commentEntity.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/moderator/comments"));
    }
}
