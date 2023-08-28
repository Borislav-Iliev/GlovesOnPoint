package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.UserRoleEnum;
import bg.softuni.kickboxing.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRoleServiceTest {

    @InjectMocks
    private UserRoleService userRoleService;

    @Mock
    private UserRoleRepository userRoleRepository;

    private static UserRoleEntity userRole;
    private static UserRoleEntity moderatorRole;

    @BeforeAll
    static void setUp() {
        userRole = initUserRole();
        moderatorRole = initModeratorRole();
    }

    @Test
    void testSeedRoles_ShouldAddRoles_WhenRoleCountIsZero() {
        when(this.userRoleRepository.count())
                .thenReturn(0L);

        this.userRoleService.seedRoles();

        verify(this.userRoleRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testSeedRoles_ShouldNotAddRoles_WhenRoleCountIsNotZero() {
        when(this.userRoleRepository.count())
                .thenReturn(1L);

        this.userRoleService.seedRoles();

        verify(this.userRoleRepository, never()).saveAll(anyList());
    }

    @Test
    void testGetUserRole_ShouldReturnUserRole() {
        when(this.userRoleRepository.findByUserRole(UserRoleEnum.USER))
                .thenReturn(Optional.of(userRole));

        UserRoleEntity actualUserRole = this.userRoleService.getUserRole();

        assertAll(
                () -> assertEquals(userRole.getId(), actualUserRole.getId()),
                () -> assertEquals(userRole.getUserRole(), actualUserRole.getUserRole())
        );
    }

    @Test
    void testGetModeratorRole_ShouldReturnModeratorRole() {
        when(this.userRoleRepository.findByUserRole(UserRoleEnum.MODERATOR))
                .thenReturn(Optional.of(moderatorRole));

        UserRoleEntity actualUserRole = this.userRoleService.getModeratorRole();

        assertAll(
                () -> assertEquals(moderatorRole.getId(), actualUserRole.getId()),
                () -> assertEquals(moderatorRole.getUserRole(), actualUserRole.getUserRole())
        );
    }

    private static UserRoleEntity initUserRole() {
        return new UserRoleEntity(UserRoleEnum.USER);
    }

    private static UserRoleEntity initModeratorRole() {
        return new UserRoleEntity(UserRoleEnum.MODERATOR);
    }
}
