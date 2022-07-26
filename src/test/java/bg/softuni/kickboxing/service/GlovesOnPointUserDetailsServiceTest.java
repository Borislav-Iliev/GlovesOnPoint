package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.UserRoleEnum;
import bg.softuni.kickboxing.model.user.GlovesOnPointUserDetails;
import bg.softuni.kickboxing.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class GlovesOnPointUserDetailsServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    private GlovesOnPointUserDetailsService toTest;

    @BeforeEach
    public void setUp() {
        this.toTest = new GlovesOnPointUserDetailsService(mockUserRepository);
    }

    @Test
    public void testLoadUserByUsername_UserExists() {
        UserEntity testUserEntity = new UserEntity()
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

        Mockito.when(this.mockUserRepository.findByUsername(testUserEntity.getUsername()))
                .thenReturn(Optional.of(testUserEntity));

        GlovesOnPointUserDetails userDetails = (GlovesOnPointUserDetails)
                this.toTest.loadUserByUsername("TestUsername");

        Assertions.assertEquals(testUserEntity.getId(), userDetails.getId());
        Assertions.assertEquals(testUserEntity.getUsername(), userDetails.getUsername());
        Assertions.assertEquals(testUserEntity.getFirstName(), userDetails.getFirstName());
        Assertions.assertEquals(testUserEntity.getLastName(), userDetails.getLastName());
        Assertions.assertEquals(testUserEntity.getEmail(), userDetails.getEmail());
        Assertions.assertEquals(testUserEntity.getPassword(), userDetails.getPassword());
        Assertions.assertEquals(testUserEntity.getImageUrl(), userDetails.getImageUrl());
        Assertions.assertEquals(testUserEntity.getFirstName() + " " + testUserEntity.getLastName(), userDetails.getFullName());
        Assertions.assertEquals(testUserEntity.getPosts(), userDetails.getPosts());
        Assertions.assertEquals(testUserEntity.getComments(), userDetails.getComments());

        String expectedRoles = "ROLE_ADMIN, ROLE_MODERATOR, ROLE_USER";
        String actualRoles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", "));
        Assertions.assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> this.toTest.loadUserByUsername("not_existing_username"));
    }
}
