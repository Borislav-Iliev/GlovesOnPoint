package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.UserRoleEnum;
import bg.softuni.kickboxing.repository.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRoleServiceTest {

    @InjectMocks
    private UserRoleService userRoleService;

    @Mock
    private UserRoleRepository userRoleRepository;

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
        UserRoleEntity expectedUserRole = initUserRole();

        when(this.userRoleRepository.findByUserRole(UserRoleEnum.USER))
                .thenReturn(Optional.of(expectedUserRole));

        UserRoleEntity actualUserRole = this.userRoleService.getUserRole();

        assertEquals(expectedUserRole.getId(), actualUserRole.getId());
        assertEquals(expectedUserRole.getUserRole(), actualUserRole.getUserRole());
    }

    @Test
    void testGetModeratorRole_ShouldReturnModeratorRole() {
        UserRoleEntity expectedUserRole = initModeratorRole();

        when(this.userRoleRepository.findByUserRole(UserRoleEnum.MODERATOR))
                .thenReturn(Optional.of(expectedUserRole));

        UserRoleEntity actualUserRole = this.userRoleService.getModeratorRole();

        assertEquals(expectedUserRole.getId(), actualUserRole.getId());
        assertEquals(expectedUserRole.getUserRole(), actualUserRole.getUserRole());
    }

    private UserRoleEntity initUserRole() {
        return new UserRoleEntity(UserRoleEnum.USER);
    }

    private UserRoleEntity initModeratorRole() {
        return new UserRoleEntity(UserRoleEnum.MODERATOR);
    }
}
