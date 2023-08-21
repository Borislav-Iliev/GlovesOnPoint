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
import bg.softuni.kickboxing.service.CommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Executable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService toTest;

    @Mock
    private ModelMapper mapper;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    private Pageable pageable;

    @Mock
    private GlovesOnPointUserDetails userDetails;

//    @BeforeEach
//    public void setUp() {
//        this.toTest = new CommentService(this.commentRepository,
//                this.mapper, this.postRepository, this.userRepository);
//    }

    @AfterEach
    public void tearDown() {
        reset(this.userRepository);
        reset(this.commentRepository);
        reset(this.postRepository);
        reset(this.mapper);
    }

    @ParameterizedTest
    @CsvSource(value = {"ADMIN", "MODERATOR"})
    void testAddComment_ShouldAddCommentAndSetIsApprovedToTrue_WhenUserIsAdmin(String userRole) {
        AddCommentDTO addCommentDTO = initAddCommentDto();
        CommentEntity comment = initComment();
        comment.setApproved(true);

        when(this.mapper.map(addCommentDTO, CommentEntity.class))
                .thenReturn(comment);

        PostEntity post = initPost();
        when(this.postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        UserEntity user = initUser();
        when(this.userDetails.getUsername())
                .thenReturn("Username");
        when(this.userDetails.getRole()).
                thenReturn(userRole);

        when(this.userRepository.findByUsername(this.userDetails.getUsername()))
                .thenReturn(Optional.of(user));

        when(this.commentRepository.save(comment))
                .thenReturn(comment);

        this.toTest.addComment(1L, addCommentDTO, userDetails);

        verify(this.commentRepository, times(1)).save(comment);
        assertTrue(comment.isApproved());
    }

    @Test
    void testAddComment_ShouldAddCommentAndSetIsApprovedToFalse_WhenUserIsANormalUser() {
        AddCommentDTO addCommentDTO = initAddCommentDto();
        CommentEntity comment = initComment();

        when(this.mapper.map(addCommentDTO, CommentEntity.class))
                .thenReturn(comment);

        PostEntity post = initPost();
        when(this.postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        UserEntity user = initUser();
        when(this.userDetails.getUsername())
                .thenReturn("Username");
        when(this.userDetails.getRole()).
                thenReturn("USER");

        when(this.userRepository.findByUsername(this.userDetails.getUsername()))
                .thenReturn(Optional.of(user));

        when(this.commentRepository.save(comment))
                .thenReturn(comment);

        this.toTest.addComment(1L, addCommentDTO, userDetails);

        verify(this.commentRepository, times(1)).save(comment);
        assertFalse(comment.isApproved());
    }

    @Test
    void testGetAllNotApprovedCommentsOrderedByDateDesc_ShouldReturnListOfCommentDto() {
        List<CommentDTO> comments = List.of(initCommentDto(), initCommentDto());
        PageImpl<CommentDTO> expectedCommentsPage = new PageImpl<>(comments);

        when(this.commentRepository.getAllNotApprovedCommentsOrderedByCreatedOnDesc(this.pageable))
                .thenReturn(expectedCommentsPage);

        Page<CommentDTO> actualCommentsPage = this.toTest
                .getAllNotApprovedCommentsOrderedByDateDesc(this.pageable);

        assertIterableEquals(expectedCommentsPage, actualCommentsPage);
    }

    @Test
    void testApproveComment_ShouldSetApprovedToTrue() {
        CommentEntity comment = initComment();

        when(this.commentRepository.findById(comment.getId()))
                .thenReturn(Optional.of(comment));

        this.toTest.approveComment(comment.getId());

        assertTrue(comment.isApproved());
    }

    @ParameterizedTest
    @CsvSource(value = {"4", "5"})
    void testApproveComment_ShouldThrowException_WhenIdIsOutOfBounds(Long id) {
        assertThrows(ObjectNotFoundException.class, () -> this.toTest.approveComment(id));
    }

    @ParameterizedTest
    @CsvSource(value = {"1", "2"})
    void testDisapproveComment_ShouldDeleteComment(Long id) {
        CommentEntity comment = initComment();

        when(this.commentRepository.findTopByOrderByIdDesc()).thenReturn(comment);

        this.toTest.disapproveComment(id);

        verify(this.commentRepository, times(1)).deleteById(id);
    }

    @ParameterizedTest
    @CsvSource(value = {"4", "5", "6", "7"})
    void testDisapproveComment_ShouldThrowException_WhenCommentIdIsOutOfBounds(Long id) {
        CommentEntity comment = initComment();

        when(this.commentRepository.findTopByOrderByIdDesc()).thenReturn(comment);

        assertThrows(ObjectNotFoundException.class, () -> this.toTest.disapproveComment(id));
    }

    @Test
    void testGetIdOfLastObjectInTable_ShouldReturnLastId() {
        CommentEntity comment = initComment();

        when(this.commentRepository.findTopByOrderByIdDesc()).thenReturn(comment);

        assertEquals(3L, comment.getId());
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

    private PostEntity initPost() {
        return new PostEntity()
                .setTitle("Title")
                .setContent("Content")
                .setCategory(PostCategoryEnum.PROBLEM)
                .setViews(0)
                .setCreatedOn(LocalDateTime.now())
                .setApproved(true);
    }

    private CommentEntity initComment() {
        CommentEntity commentEntity = new CommentEntity()
                .setContent("Content")
                .setApproved(false)
                .setCreatedOn(LocalDateTime.now())
                .setPost(this.initPost())
                .setAuthor(this.initUser());
        commentEntity.setId(3L);
        return commentEntity;
    }

    private CommentDTO initCommentDto() {
        return new CommentDTO(
                1L, "Content", LocalDateTime.now(), "Username", "image:/url"
        );
    }

    private AddCommentDTO initAddCommentDto() {
        AddCommentDTO addCommentDTO = new AddCommentDTO();
        addCommentDTO.setContent("Content");
        return addCommentDTO;
    }
}
