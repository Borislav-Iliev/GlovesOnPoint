package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.dto.comment.AddCommentDTO;
import bg.softuni.kickboxing.model.dto.comment.CommentDTO;
import bg.softuni.kickboxing.model.entity.CommentEntity;
import bg.softuni.kickboxing.model.entity.PostEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.PostCategoryEnum;
import bg.softuni.kickboxing.model.enums.UserRoleEnum;
import bg.softuni.kickboxing.model.exception.ObjectNotFoundException;
import bg.softuni.kickboxing.model.user.GlovesOnPointUserDetails;
import bg.softuni.kickboxing.repository.CommentRepository;
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
public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private ModelMapper mapper;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private Pageable pageable;

    @Mock
    private GlovesOnPointUserDetails userDetails;

    private static CommentEntity comment;
    private static AddCommentDTO addCommentDto;
    private static PostEntity post;
    private static UserEntity user;
    private static PageImpl<CommentDTO> expectedCommentsPage;

    @BeforeAll
    static void setUp() {
        comment = initComment();
        addCommentDto = initAddCommentDto();
        post = initPost();
        user = initUser();
        List<CommentDTO> comments = List.of(initCommentDto(), initCommentDto());
        expectedCommentsPage = new PageImpl<>(comments);
    }

    @ParameterizedTest
    @CsvSource(value = {"ADMIN", "MODERATOR"})
    void testAddComment_ShouldAddCommentAndSetIsApprovedToTrue_WhenUserIsAdmin(String userRole) {
        comment.setApproved(true);
        when(this.mapper.map(addCommentDto, CommentEntity.class))
                .thenReturn(comment);

        when(this.postRepository.findById(anyLong()))
                .thenReturn(Optional.of(post));

        when(this.userDetails.getUsername())
                .thenReturn("Username");
        when(this.userDetails.getRole())
                .thenReturn(userRole);

        when(this.userRepository.findByUsername(this.userDetails.getUsername()))
                .thenReturn(Optional.of(user));

        when(this.commentRepository.save(comment))
                .thenReturn(comment);

        this.commentService.addComment(1L, addCommentDto, userDetails);

        verify(this.commentRepository, times(1)).save(comment);
        assertTrue(comment.isApproved());
    }

    @Test
    void testAddComment_ShouldAddCommentAndSetIsApprovedToFalse_WhenUserIsANormalUser() {
        when(this.mapper.map(addCommentDto, CommentEntity.class))
                .thenReturn(comment);

        when(this.postRepository.findById(anyLong()))
                .thenReturn(Optional.of(post));

        when(this.userDetails.getUsername())
                .thenReturn("Username");
        when(this.userDetails.getRole()).
                thenReturn("USER");

        when(this.userRepository.findByUsername(this.userDetails.getUsername()))
                .thenReturn(Optional.of(user));

        when(this.commentRepository.save(comment))
                .thenReturn(comment);

        this.commentService.addComment(1L, addCommentDto, userDetails);

        verify(this.commentRepository, times(1)).save(comment);
        assertFalse(comment.isApproved());
    }

    @Test
    void testGetAllNotApprovedCommentsOrderedByDateDesc_ShouldReturnListOfCommentDto() {
        when(this.commentRepository.getAllNotApprovedCommentsOrderedByCreatedOnDesc(this.pageable))
                .thenReturn(expectedCommentsPage);

        Page<CommentDTO> actualCommentsPage = this.commentService
                .getAllNotApprovedCommentsOrderedByDateDesc(this.pageable);

        assertIterableEquals(expectedCommentsPage, actualCommentsPage);
    }

    @Test
    void testApproveComment_ShouldSetApprovedToTrue() {
        when(this.commentRepository.findById(anyLong()))
                .thenReturn(Optional.of(comment));

        this.commentService.approveComment(comment.getId());

        assertTrue(comment.isApproved());
    }

    @ParameterizedTest
    @CsvSource(value = {"4", "5"})
    void testApproveComment_ShouldThrowException_WhenIdIsOutOfBounds(Long id) {
        Executable executable = () -> this.commentService.approveComment(id);

        assertThrows(ObjectNotFoundException.class, executable);
    }

    @ParameterizedTest
    @CsvSource(value = {"1", "2"})
    void testDisapproveComment_ShouldDeleteComment(Long id) {
        when(this.commentRepository.findTopByOrderByIdDesc()).thenReturn(comment);

        this.commentService.disapproveComment(id);

        verify(this.commentRepository, times(1)).deleteById(id);
    }

    @ParameterizedTest
    @CsvSource(value = {"4", "5", "6", "7"})
    void testDisapproveComment_ShouldThrowException_WhenCommentIdIsOutOfBounds(Long id) {
        when(this.commentRepository.findTopByOrderByIdDesc()).thenReturn(comment);

        assertThrows(ObjectNotFoundException.class, () -> this.commentService.disapproveComment(id));
    }

    @Test
    void testGetIdOfLastObjectInTable_ShouldReturnLastId() {
        when(this.commentRepository.findTopByOrderByIdDesc()).thenReturn(comment);

        Long actualLastId = this.commentService.getIdOfLastObjectInTable();

        assertEquals(comment.getId(), actualLastId);
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

    private static PostEntity initPost() {
        return new PostEntity()
                .setTitle("Title")
                .setContent("Content")
                .setCategory(PostCategoryEnum.PROBLEM)
                .setViews(0)
                .setCreatedOn(LocalDateTime.now())
                .setApproved(true);
    }

    private static CommentEntity initComment() {
        CommentEntity commentEntity = new CommentEntity()
                .setContent("Content")
                .setApproved(false)
                .setCreatedOn(LocalDateTime.now())
                .setPost(initPost())
                .setAuthor(initUser());
        commentEntity.setId(3L);
        return commentEntity;
    }

    private static CommentDTO initCommentDto() {
        return new CommentDTO(
                1L, "Content", LocalDateTime.now(), "Username", "image:/url"
        );
    }

    private static AddCommentDTO initAddCommentDto() {
        AddCommentDTO addCommentDTO = new AddCommentDTO();
        addCommentDTO.setContent("Content");
        return addCommentDTO;
    }
}
