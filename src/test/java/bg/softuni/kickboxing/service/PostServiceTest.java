package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.dto.comment.CommentDTO;
import bg.softuni.kickboxing.model.dto.post.AddPostDTO;
import bg.softuni.kickboxing.model.dto.post.PostDTO;
import bg.softuni.kickboxing.model.dto.post.PostDetailsDTO;
import bg.softuni.kickboxing.model.entity.CommentEntity;
import bg.softuni.kickboxing.model.entity.PostEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.PostCategoryEnum;
import bg.softuni.kickboxing.model.enums.UserRoleEnum;
import bg.softuni.kickboxing.model.exception.ObjectNotFoundException;
import bg.softuni.kickboxing.model.user.GlovesOnPointUserDetails;
import bg.softuni.kickboxing.repository.PostRepository;
import bg.softuni.kickboxing.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Mock
    private Pageable pageable;

    @Mock
    private GlovesOnPointUserDetails userDetails;

    private static PostEntity post;
    private static AddPostDTO addPostDto;
    private static PostDetailsDTO postDetailsDto;
    private static UserEntity user;
    private static Page<PostDTO> expectedPostsPage;

    @BeforeAll
    static void setUp() {
        post = initPost();
        addPostDto = initAddPostDto();
        postDetailsDto = initPostDetailsDto();
        user = initUser();
        List<PostDTO> posts = List.of(initPostDto());
        expectedPostsPage = new PageImpl<>(posts);
    }

    @ParameterizedTest
    @CsvSource(value = {"ADMIN", "MODERATOR"})
    void testAddPost_ShouldSetApprovedToTrueAndAddPost_WhenUserIsAdminOrModeratorAndCorrectDataIsPassed(String role) {
        when(this.mapper.map(addPostDto, PostEntity.class))
                .thenReturn(post);

        when(this.userDetails.getUsername())
                .thenReturn("Username");
        when(this.userRepository.findByUsername(this.userDetails.getUsername()))
                .thenReturn(Optional.of(user));

        when(this.userDetails.getRole())
                .thenReturn(role);

        this.postService.addPost(addPostDto, this.userDetails);

        assertTrue(post.isApproved());
        verify(this.postRepository, times(1)).save(post);
    }

    @ParameterizedTest
    @CsvSource(value = {"USER"})
    void testAddPost_ShouldSetApprovedToFalseAndAddPost_WhenUserIsANormalUserAndCorrectDataIsPassed(String role) {
        post = initPost();
        when(this.mapper.map(addPostDto, PostEntity.class))
                .thenReturn(post);

        when(this.userDetails.getUsername())
                .thenReturn("Username");
        when(this.userRepository.findByUsername(this.userDetails.getUsername()))
                .thenReturn(Optional.of(user));

        when(this.userDetails.getRole())
                .thenReturn(role);

        this.postService.addPost(addPostDto, this.userDetails);

        assertFalse(post.isApproved());
        verify(this.postRepository, times(1)).save(post);
    }

    @Test
    void testGetAllApprovedPostsOrderedByDateDesc_ShouldReturnCorrectPageOfPosts() {
        when(this.postRepository.getAllApprovedPostsOrderedByCreatedOnDesc(this.pageable))
                .thenReturn(expectedPostsPage);

        Page<PostDTO> actualPostsPage = this.postService.getAllApprovedPostsOrderedByDateDesc(this.pageable);

        assertIterableEquals(expectedPostsPage, actualPostsPage);
    }

    @Test
    void testAddPost_ShouldThrowException_WhenUserHasAnyRoleAndUserCannotBeFound() {
        Executable executable = () -> this.postService.addPost(addPostDto, this.userDetails);

        assertThrows(RuntimeException.class, executable);
    }

    @Test
    void testGetAllApprovedPostsByCategoryOrderedByCreatedOnDesc_ShouldReturnCorrectPageOfPosts() {
        when(this.postRepository.getAllApprovedPostsByCategoryOrderedByCreatedOnDesc(this.pageable, PostCategoryEnum.ARTICLE))
                .thenReturn(expectedPostsPage);

        Page<PostDTO> actualPostsPage = this.postService
                .getAllApprovedPostsByCategoryOrderedByCreatedOnDesc(this.pageable, PostCategoryEnum.ARTICLE.name());

        assertIterableEquals(expectedPostsPage, actualPostsPage);
    }

    @ParameterizedTest
    @CsvSource(value = {"T", "Ti", "I"})
    void testGetAllApprovedPostsWhereTitleLike_ShouldReturnCorrectPageOfPosts(String title) {
        when(this.postRepository.getAllApprovedPostsWhereTitleLike(this.pageable, title))
                .thenReturn(expectedPostsPage);

        Page<PostDTO> actualPostsPage = this.postService
                .getAllApprovedPostsWhereTitleLike(this.pageable, title);

        assertIterableEquals(expectedPostsPage, actualPostsPage);
    }

    @Test
    void testGetAllNotApprovedPostsOrderedByDateDesc_ShouldReturnCorrectPageOfPosts() {
        when(this.postRepository.getAllNotApprovedPostsOrderedByCreatedOnDesc(this.pageable))
                .thenReturn(expectedPostsPage);

        Page<PostDTO> actualPostsPage = this.postService
                .getAllNotApprovedPostsOrderedByDateDesc(this.pageable);

        assertIterableEquals(expectedPostsPage, actualPostsPage);
    }

    @ParameterizedTest
    @CsvSource(value = {"1", "2", "3", "4"})
    void testFindById_ShouldReturnCorrectPost_WhenCorrectPostIdIsPassed(Long id) {
        post.setId(id);

        when(this.postRepository.findById(id))
                .thenReturn(Optional.of(post));

        PostEntity actualPost = this.postService.findById(id);

        assertAll(
                () -> assertEquals(post.getId(), actualPost.getId()),
                () -> assertEquals(post.getTitle(), actualPost.getTitle()),
                () -> assertEquals(post.getContent(), actualPost.getContent()),
                () -> assertEquals(post.getCategory(), actualPost.getCategory()),
                () -> assertEquals(post.getViews(), actualPost.getViews()),
                () -> assertEquals(post.getCreatedOn(), actualPost.getCreatedOn()),
                () -> assertEquals(post.isApproved(), actualPost.isApproved()),
                () -> assertIterableEquals(post.getComments(), actualPost.getComments()),
                () -> assertSame(post.getAuthor(), actualPost.getAuthor())
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"-1", "-2", "-3", "-4"})
    void testFindById_ShouldThrowException_WhenIncorrectPostIdIsPassed(Long id) {
        Executable executable = () -> this.postService.findById(id);

        assertThrows(ObjectNotFoundException.class, executable);
    }

    @ParameterizedTest
    @CsvSource(value = {"1", "2", "3", "4"})
    void testGetPostDetailsById_ShouldReturnCorrectPost_WhenValidPostIdIsPassed(Long id) {
        when(this.postRepository.findById(id))
                .thenReturn(Optional.of(post));

        when(this.mapper.map(post, PostDetailsDTO.class))
                .thenReturn(postDetailsDto);

        PostDetailsDTO actualPostDetailsDto = this.postService.getPostDetailsById(id);

        assertAll(
                () -> assertEquals(postDetailsDto.getId(), actualPostDetailsDto.getId()),
                () -> assertEquals(postDetailsDto.getTitle(), actualPostDetailsDto.getTitle()),
                () -> assertEquals(postDetailsDto.getContent(), actualPostDetailsDto.getContent()),
                () -> assertEquals(postDetailsDto.getCategory(), actualPostDetailsDto.getCategory()),
                () -> assertEquals(postDetailsDto.getViews(), actualPostDetailsDto.getViews()),
                () -> assertEquals(postDetailsDto.getCreatedOn(), actualPostDetailsDto.getCreatedOn()),
                () -> assertIterableEquals(postDetailsDto.getComments(), actualPostDetailsDto.getComments()),
                () -> assertEquals(postDetailsDto.getAuthorUsername(), actualPostDetailsDto.getAuthorUsername()),
                () -> assertEquals(postDetailsDto.getAuthorImageUrl(), actualPostDetailsDto.getAuthorImageUrl())
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"-1", "-2", "-3", "-4"})
    void testGetPostDetailsById_ShouldThrowException_WhenInvalidPostIdIsPassed(Long id) {
        Executable executable = () -> this.postService.getPostDetailsById(id);

        assertThrows(ObjectNotFoundException.class, executable);
    }

    @ParameterizedTest
    @CsvSource(value = {"1", "2", "3", "4"})
    void testIncreaseViewsCount_ShouldIncreaseViewsCountOfPost_WhenValidPostIdIsPassed(Long id) {
        when(this.postRepository.findById(id))
                .thenReturn(Optional.of(post));

        this.postService.increaseViewsCount(id);

        verify(this.postRepository, times(1)).save(post);
    }

    @ParameterizedTest
    @CsvSource(value = {"-1", "-2", "-3", "-4"})
    void testIncreaseViewsCount_ShouldThrowException_WhenInvalidPostIdIsPassed(Long id) {
        when(this.postRepository.findById(id))
                .thenThrow(RuntimeException.class);

        Executable executable = () -> this.postService.increaseViewsCount(id);

        assertThrows(RuntimeException.class, executable);
    }

    @ParameterizedTest
    @CsvSource(value = {"1", "2", "3", "4"})
    void testApprovePost_ShouldSetApprovedToTrueAndSavePost_WhenValidPostIdIsPassed(Long id) {
        when(this.postRepository.findById(id))
                .thenReturn(Optional.of(post));

        this.postService.approvePost(id);

        assertTrue(post.isApproved());
        verify(this.postRepository, times(1)).save(post);
    }

    @ParameterizedTest
    @CsvSource(value = {"-1", "-2", "-3", "-4"})
    void testApprovePost_ShouldThrowException_WhenInvalidPostIdIsPassed(Long id) {
        Executable executable = () -> this.postService.approvePost(id);

        assertThrows(ObjectNotFoundException.class, executable);
    }

    @ParameterizedTest
    @CsvSource(value = {"1", "2", "3", "4"})
    void testDisapprovePost_ShouldDeletePost_WhenValidPostIdIsPassed(Long id) {
        post.setId(id);
        when(this.postRepository.findTopByOrderByIdDesc())
                .thenReturn(post);

        this.postService.disapprovePost(id);

        verify(this.postRepository, times(1)).deleteById(id);
    }

    @ParameterizedTest
    @CsvSource(value = {"3", "4"})
    void testDisapprovePost_ShouldThrowException_WhenInvalidPostIdIsPassed(Long id) {
        post.setId(2L);
        when(this.postRepository.findTopByOrderByIdDesc())
                .thenReturn(post);

        Executable executable = () -> this.postService.disapprovePost(id);

        assertThrows(ObjectNotFoundException.class, executable);
    }

    @ParameterizedTest
    @CsvSource(value = {"1", "2", "3", "4"})
    void testGetIdOfLastObjectInTable_ShouldReturnIdOfLastObject(Long id) {
        post.setId(id);
        when(this.postRepository.findTopByOrderByIdDesc())
                .thenReturn(post);

        Long actualLastId = this.postService.getIdOfLastObjectInTable();

        assertEquals(post.getId(), actualLastId);
    }

    private static PostEntity initPost() {
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

    private static CommentEntity initComment() {
        return new CommentEntity()
                .setContent("Content")
                .setApproved(false)
                .setCreatedOn(LocalDateTime.now())
                .setPost(null)
                .setAuthor(initUser());
    }

    private static UserEntity initUser() {
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

    private static AddPostDTO initAddPostDto() {
        AddPostDTO addPostDTO = new AddPostDTO();
        addPostDTO.setTitle("Title");
        addPostDTO.setContent("Content");
        addPostDTO.setCategory(PostCategoryEnum.ARTICLE);
        return addPostDTO;
    }

    private static PostDTO initPostDto() {
        return new PostDTO(
                1L, "Username", "image:/url", 1L,
                "Title", PostCategoryEnum.ARTICLE, 1, LocalDateTime.now()
        );
    }

    private static PostDetailsDTO initPostDetailsDto() {
        return new PostDetailsDTO(
                1L, "Title", "Content", PostCategoryEnum.ARTICLE, 1,
                LocalDateTime.now(), List.of(initCommentDto()), "Username", "image:/url"
        );
    }

    private static CommentDTO initCommentDto() {
        return new CommentDTO(
                1L, "Content", LocalDateTime.now(), "Username", "image:/url"
        );
    }
}
