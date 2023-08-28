package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.dto.user.EditUserDTO;
import bg.softuni.kickboxing.model.dto.user.TopUserDTO;
import bg.softuni.kickboxing.model.dto.user.UserDTO;
import bg.softuni.kickboxing.model.dto.user.UserRegistrationDTO;
import bg.softuni.kickboxing.model.entity.CommentEntity;
import bg.softuni.kickboxing.model.entity.PostEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.PostCategoryEnum;
import bg.softuni.kickboxing.model.enums.UserRoleEnum;
import bg.softuni.kickboxing.model.exception.UsernameNotFoundException;
import bg.softuni.kickboxing.model.user.GlovesOnPointUserDetails;
import bg.softuni.kickboxing.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    @Spy
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleService userRoleService;

    @Mock
    private ModelMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private EmailService emailService;

    @Mock
    private GlovesOnPointUserDetails userDetails;

    private static List<TopUserDTO> topUsers;
    private static UserEntity user;
    private static UserRegistrationDTO userRegistrationDto;
    private static UserDTO userDto;

    @BeforeAll
    static void setUp() {
        topUsers = List.of(initTopUserDto(), initTopUserDto());
        user = initUser();
        userRegistrationDto = initUserRegistrationDto();
        userDto = initUserDto();
    }

    @Test
    void testRegisterAndLogin_ShouldRegisterAndLoginTheUser() {
        when(this.mapper.map(userRegistrationDto, UserEntity.class))
                .thenReturn(user);

        when(this.passwordEncoder.encode(userRegistrationDto.getPassword()))
                .thenReturn(userRegistrationDto.getPassword());

        UserRoleEntity userRole = initUserRole();
        when(this.userRoleService.getUserRole())
                .thenReturn(userRole);

        when(this.userDetailsService.loadUserByUsername(user.getUsername()))
                .thenReturn(this.userDetails);

        this.userService.registerAndLogin(userRegistrationDto);

        verify(this.userRepository, times(1)).save(user);
        verify(this.emailService, times(1))
                .sendRegistrationEmail(user.getEmail(), user.getFirstName() + " " + user.getLastName());
        verify(this.userService, times(1)).login(user);
    }

    @ParameterizedTest
    @CsvSource(value = {"Username", "Test", "TestUsername"})
    void testGetUserDTOByUsername_ShouldReturnCorrectUserDto_WhenValidUsernameIsPassed(String username) {
        when(this.userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        when(this.mapper.map(user, UserDTO.class))
                .thenReturn(userDto);

        UserDTO expectedUserDto = this.userService.getUserDTOByUsername(username);

        assertAll(
                () -> assertEquals(userDto.getId(), expectedUserDto.getId()),
                () -> assertEquals(userDto.getUsername(), expectedUserDto.getUsername()),
                () -> assertEquals(userDto.getEmail(), expectedUserDto.getEmail()),
                () -> assertEquals(userDto.getFirstName(), expectedUserDto.getFirstName()),
                () -> assertEquals(userDto.getLastName(), expectedUserDto.getLastName()),
                () -> assertEquals(userDto.getImageUrl(), expectedUserDto.getImageUrl()),
                () -> assertIterableEquals(userDto.getUserRoles(), expectedUserDto.getUserRoles()),
                () -> assertIterableEquals(userDto.getPosts(), expectedUserDto.getPosts()),
                () -> assertIterableEquals(userDto.getComments(), expectedUserDto.getComments())
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"Username1", "Test1", "TestUsername1"})
    void testGetUserDTOByUsername_ShouldThrowException_WhenInvalidUsernameIsPassed(String username) {
        Executable executable = () -> this.userService.getUserDTOByUsername(username);

        assertThrows(UsernameNotFoundException.class, executable);
    }

    @Test
    void testGetUserDTOById_ShouldReturnCorrectUserDto_WhenValidUserIdIsPassed() {
        when(this.userRepository.findByUsername("Username"))
                .thenReturn(Optional.of(user));

        when(this.mapper.map(user, UserDTO.class))
                .thenReturn(userDto);

        UserDTO expectedUserDto = this.userService.getUserDTOById("Username");

        assertAll(
                () -> assertEquals(userDto.getId(), expectedUserDto.getId()),
                () -> assertEquals(userDto.getUsername(), expectedUserDto.getUsername()),
                () -> assertEquals(userDto.getEmail(), expectedUserDto.getEmail()),
                () -> assertEquals(userDto.getFirstName(), expectedUserDto.getFirstName()),
                () -> assertEquals(userDto.getLastName(), expectedUserDto.getLastName()),
                () -> assertEquals(userDto.getImageUrl(), expectedUserDto.getImageUrl()),
                () -> assertIterableEquals(userDto.getUserRoles(), expectedUserDto.getUserRoles()),
                () -> assertIterableEquals(userDto.getPosts(), expectedUserDto.getPosts()),
                () -> assertIterableEquals(userDto.getComments(), expectedUserDto.getComments())
        );
    }

    @Test
    void testGetUserDTOById_ShouldThrowException_WhenInvalidUserIdIsPassed() {
        when(this.userRepository.findByUsername(anyString()))
                .thenThrow(RuntimeException.class);

        Executable executable = () -> this.userService.getUserDTOById(anyString());

        assertThrows(RuntimeException.class, executable);
    }

    @Test
    void testGetTopProfiles_ShouldReturnCorrectListOfTopUserDto() {
        when(this.userRepository.getAllDistinctByOrderByPostsDesc())
                .thenReturn(topUsers);

        List<TopUserDTO> actualTopUsers = this.userService.getTopProfiles();

        assertIterableEquals(topUsers, actualTopUsers);
    }

    @ParameterizedTest
    @CsvSource(value = {"Username", "Test", "TestUsername"})
    void testMakeModerator_ShouldAddModeratorRoleToUser_WhenValidUsernameIsPassed(String username) {
        user = initNormalUser();
        when(this.userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        UserRoleEntity moderatorRole = initModeratorRole();
        when(this.userRoleService.getModeratorRole())
                .thenReturn(moderatorRole);

        this.userService.makeModerator(username);

        String expectedUserRoles = "USER, MODERATOR";
        String actualUserRoles = user.getUserRoles().stream().map(r -> r.getUserRole().name()).collect(Collectors.joining(", "));

        assertEquals(expectedUserRoles, actualUserRoles);
        verify(this.userRepository, times(1)).save(user);
    }

    @ParameterizedTest
    @CsvSource(value = {"Username1", "Test1", "TestUsername1"})
    void testMakeModerator_ShouldThrowException_WhenInvalidUsernameIsPassed(String username) {
        Executable executable = () -> this.userService.makeModerator(username);

        assertThrows(UsernameNotFoundException.class, executable);
    }

    @ParameterizedTest
    @CsvSource(value = {"Username", "Test", "TestUsername"})
    void testRemoveModerator_ShouldRemoveModeratorRole_WhenValidUsernameIsPassed(String username) {
        user = initModerator();
        when(this.userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        this.userService.removeModerator(username);

        String expectedRoles = "USER";
        String actualRoles = user.getUserRoles().stream().map(r -> r.getUserRole().name()).collect(Collectors.joining(", "));

        assertEquals(expectedRoles, actualRoles);
        verify(this.userRepository, times(1)).save(user);
    }

    @ParameterizedTest
    @CsvSource(value = {"Username1", "Test1", "TestUsername1"})
    void testRemoveModerator_ShouldThrowException_WhenInvalidUsernameIsPassed(String username) {
        Executable executable = () -> this.userService.removeModerator(username);

        assertThrows(UsernameNotFoundException.class, executable);
    }

    @ParameterizedTest
    @CsvSource(value = {"Username", "Test", "TestUsername"})
    void testEditProfile_ShouldEditProfile_WhenValidUsernameIsPassed(String username) {
        when(this.userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        EditUserDTO editUserDto = initEditUserDto();
        when(this.passwordEncoder.encode(editUserDto.getPassword()))
                .thenReturn(editUserDto.getPassword());

        this.userService.editProfile(editUserDto, username);

        assertAll(
                () -> assertEquals(user.getUsername(), editUserDto.getUsername()),
                () -> assertEquals(user.getFirstName(), editUserDto.getFirstName()),
                () -> assertEquals(user.getLastName(), editUserDto.getLastName()),
                () -> assertEquals(user.getImageUrl(), editUserDto.getImageUrl()),
                () -> assertEquals(user.getPassword(), editUserDto.getPassword())
        );
        verify(this.userRepository, times(1)).save(user);
    }

    @ParameterizedTest
    @CsvSource(value = {"Username", "Test", "TestUsername"})
    void testEditProfile_ShouldSkipField_WhenFieldIsNotValid(String username) {
        when(this.userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        EditUserDTO editUserDto = initEditUserDto();
        editUserDto.setUsername("");
        editUserDto.setFirstName("");
        editUserDto.setLastName("");
        editUserDto.setPassword("");
        editUserDto.setImageUrl("");

        this.userService.editProfile(editUserDto, username);

        assertAll(
                () -> assertNotEquals(user.getUsername(), editUserDto.getUsername()),
                () -> assertNotEquals(user.getFirstName(), editUserDto.getFirstName()),
                () -> assertNotEquals(user.getLastName(), editUserDto.getLastName()),
                () -> assertNotEquals(user.getImageUrl(), editUserDto.getImageUrl()),
                () -> assertNotEquals(user.getPassword(), editUserDto.getPassword())
        );
        verify(this.userRepository, times(1)).save(user);
    }

    @ParameterizedTest
    @CsvSource(value = {"Username", "Test", "TestUsername"})
    void testEditProfile_ShouldThrowException_WhenInvalidUsernameIsPassed(String username) {
        EditUserDTO editUserDto = initEditUserDto();

        Executable executable = () -> this.userService.editProfile(editUserDto, username);

        assertThrows(RuntimeException.class, executable);
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

    private UserEntity initNormalUser() {
        List<UserRoleEntity> roles = new ArrayList<>();
        roles.add(new UserRoleEntity(UserRoleEnum.USER));
        return new UserEntity()
                .setUsername("TestUsername")
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("test@example.com")
                .setPassword("testPassword")
                .setAge(20)
                .setImageUrl("image:/url")
                .setUserRoles(roles);
    }

    private UserEntity initModerator() {
        List<UserRoleEntity> roles = new ArrayList<>();
        roles.add(new UserRoleEntity(UserRoleEnum.USER));
        roles.add(new UserRoleEntity(UserRoleEnum.MODERATOR));
        return new UserEntity()
                .setUsername("TestUsername")
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("test@example.com")
                .setPassword("testPassword")
                .setAge(20)
                .setImageUrl("image:/url")
                .setUserRoles(roles);
    }

    private static UserRegistrationDTO initUserRegistrationDto() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setUsername("Username");
        userRegistrationDTO.setFirstName("FirstName");
        userRegistrationDTO.setLastName("LastName");
        userRegistrationDTO.setEmail("email@example.com");
        userRegistrationDTO.setAge(20);
        userRegistrationDTO.setImageUrl("image:/url");
        userRegistrationDTO.setPassword("Password");
        userRegistrationDTO.setConfirmPassword("Password");
        return userRegistrationDTO;
    }

    private static UserDTO initUserDto() {
        return new UserDTO()
                .setId(1L).setUsername("Username").setEmail("email@example.com").setFirstName("FirstName")
                .setLastName("LastName").setImageUrl("image:/url").setUserRoles(List.of(new UserRoleEntity(UserRoleEnum.USER)))
                .setPosts(List.of(initPost())).setComments(List.of(initComment()));
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
                .setAuthor(null);
    }

    private static CommentEntity initComment() {
        return new CommentEntity()
                .setContent("Content")
                .setApproved(false)
                .setCreatedOn(LocalDateTime.now())
                .setPost(null)
                .setAuthor(null);
    }

    private static TopUserDTO initTopUserDto() {
        return new TopUserDTO(
                "Username", "FirstName", "LastName", 1, 1
        );
    }

    private EditUserDTO initEditUserDto() {
        EditUserDTO editUserDTO = new EditUserDTO();
        editUserDTO.setUsername("EditUsername");
        editUserDTO.setFirstName("EditFirstName");
        editUserDTO.setLastName("EditLastName");
        editUserDTO.setEmail("EditEmail@editEmail.com");
        editUserDTO.setImageUrl("EditImageUrl");
        editUserDTO.setPassword("EditPassword");
        editUserDTO.setConfirmPassword("EditPassword");
        return editUserDTO;
    }

    private UserRoleEntity initModeratorRole() {
        return new UserRoleEntity(UserRoleEnum.MODERATOR);
    }

    private UserRoleEntity initUserRole() {
        return new UserRoleEntity(UserRoleEnum.USER);
    }
}
