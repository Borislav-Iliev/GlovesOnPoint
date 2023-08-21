package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.entity.CommentEntity;
import bg.softuni.kickboxing.model.entity.PostEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.PostCategoryEnum;
import bg.softuni.kickboxing.model.enums.UserRoleEnum;
import bg.softuni.kickboxing.model.exception.ObjectNotFoundException;
import bg.softuni.kickboxing.repository.PostRepository;
import bg.softuni.kickboxing.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    @ParameterizedTest
    @CsvSource(value = {"1", "2", "3", "4"})
    void testFindById_ShouldReturnCorrectPost_WhenCorrectPostIdIsPassed(Long id) {
        PostEntity expectedPost = initPost();
        expectedPost.setId(id);

        when(this.postRepository.findById(id))
                .thenReturn(Optional.of(expectedPost));

        PostEntity actualPost = this.postService.findById(id);

        assertEquals(expectedPost.getId(), actualPost.getId());
        assertEquals(expectedPost.getTitle(), actualPost.getTitle());
        assertEquals(expectedPost.getContent(), actualPost.getContent());
        assertEquals(expectedPost.getCategory(), actualPost.getCategory());
        assertEquals(expectedPost.getViews(), actualPost.getViews());
        assertEquals(expectedPost.getCreatedOn(), actualPost.getCreatedOn());
        assertEquals(expectedPost.isApproved(), actualPost.isApproved());
        assertIterableEquals(expectedPost.getComments(), actualPost.getComments());
        assertSame(expectedPost.getAuthor(), actualPost.getAuthor());
    }

    @ParameterizedTest
    @CsvSource(value = {"-1", "-2", "-3", "-4"})
    void testFindById_ShouldThrowException_WhenIncorrectPostIdIsPassed(Long id) {
        assertThrows(ObjectNotFoundException.class, () -> this.postService.findById(id));
    }

    private PostEntity initPost() {
        return new PostEntity()
                .setTitle("Title")
                .setContent("Content")
                .setCategory(PostCategoryEnum.ARTICLE)
                .setViews(1)
                .setCreatedOn(LocalDateTime.now())
                .setApproved(false)
                .setComments(List.of(initComment()))
                .setAuthor(initUser());
    }

    private CommentEntity initComment() {
        return new CommentEntity()
                .setContent("Content")
                .setApproved(false)
                .setCreatedOn(LocalDateTime.now())
                .setPost(null)
                .setAuthor(initUser());
    }

    private UserEntity initUser() {
        return new UserEntity()
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
    }
}
