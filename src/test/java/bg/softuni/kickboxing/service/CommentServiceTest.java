import bg.softuni.kickboxing.model.entity.CommentEntity;
import bg.softuni.kickboxing.model.entity.PostEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.PostCategoryEnum;
import bg.softuni.kickboxing.model.enums.UserRoleEnum;
import bg.softuni.kickboxing.repository.CommentRepository;
import bg.softuni.kickboxing.repository.PostRepository;
import bg.softuni.kickboxing.repository.UserRepository;
import bg.softuni.kickboxing.service.CommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Autowired
    private CommentService toTest;

    @Autowired
    private ModelMapper mapper;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @BeforeEach
    public void setUp() {
        this.toTest = new CommentService(this.commentRepository,
                this.mapper, this.postRepository, this.userRepository);
    }

    @AfterEach
    public void tearDown() {
        this.userRepository.deleteAll();
        this.commentRepository.deleteAll();
        this.postRepository.deleteAll();
        initComment();
    }

    @Test
    public void testApprovePostShouldSetApprovedToTrue() {
        CommentEntity comment = initComment();

        when(this.commentRepository.findById(comment.getId()))
                .thenReturn(Optional.of(comment));

        this.toTest.approveComment(comment.getId());

        Assertions.assertTrue(comment.isApproved());
    }

    private UserEntity initUser() {
        UserEntity userEntity = new UserEntity()
                .setUsername("TestUsername")
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("test@example.com")
                .setPassword("testPassword")
                .setAge(20)
                .setImageUrl("image:/url")
                .setUserRoles(List.of(new UserRoleEntity(UserRoleEnum.ADMIN),
                        new UserRoleEntity(UserRoleEnum.MODERATOR),
                        new UserRoleEntity(UserRoleEnum.USER)));
        this.userRepository.save(userEntity);
        return userEntity;
    }

    private PostEntity initPost() {
        PostEntity postEntity = new PostEntity()
                .setTitle("Title")
                .setContent("Content")
                .setCategory(PostCategoryEnum.PROBLEM)
                .setViews(0)
                .setCreatedOn(LocalDateTime.now())
                .setApproved(true);
        this.postRepository.save(postEntity);
        return postEntity;
    }

    private CommentEntity initComment() {
        CommentEntity commentEntity = new CommentEntity()
                .setContent("Content")
                .setApproved(false)
                .setCreatedOn(LocalDateTime.now())
                .setPost(this.initPost())
                .setAuthor(this.initUser());
        commentEntity.setId(1L);
        this.commentRepository.save(commentEntity);
        return commentEntity;
    }
}
