package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.UserRoleEnum;
import bg.softuni.kickboxing.model.user.GlovesOnPointUserDetails;
import bg.softuni.kickboxing.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class GlovesOnPointUserDetailsServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private GlovesOnPointUserDetailsService glovesOnPointUserDetailsService;

    private static UserEntity user;

    @BeforeAll
    static void setUp() {
        user = initUser();
    }

    @Test
    public void testLoadUserByUsername_WhenUserExists() {
        Mockito.when(this.mockUserRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(user));

        GlovesOnPointUserDetails userDetails = (GlovesOnPointUserDetails)
                this.glovesOnPointUserDetailsService.loadUserByUsername(anyString());

        assertAll(
                () -> assertEquals(user.getId(), userDetails.getId()),
                () -> assertEquals(user.getUsername(), userDetails.getUsername()),
                () -> assertEquals(user.getFirstName(), userDetails.getFirstName()),
                () -> assertEquals(user.getLastName(), userDetails.getLastName()),
                () -> assertEquals(user.getEmail(), userDetails.getEmail()),
                () -> assertEquals(user.getPassword(), userDetails.getPassword()),
                () -> assertEquals(user.getImageUrl(), userDetails.getImageUrl()),
                () -> assertEquals(user.getFirstName() + " " + user.getLastName(), userDetails.getFullName()),
                () -> assertEquals(user.getPosts(), userDetails.getPosts()),
                () -> assertEquals(user.getComments(), userDetails.getComments())
        );

        String expectedRoles = "ROLE_ADMIN, ROLE_MODERATOR, ROLE_USER";
        String actualRoles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", "));
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testLoadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        Executable executable = () -> this.glovesOnPointUserDetailsService.loadUserByUsername(anyString());

        assertThrows(UsernameNotFoundException.class, executable);
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
}
