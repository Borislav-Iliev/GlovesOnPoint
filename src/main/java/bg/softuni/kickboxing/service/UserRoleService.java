package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.UserRoleEnum;
import bg.softuni.kickboxing.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public void seedRoles() {
        if (this.userRoleRepository.count() == 0) {
            List<UserRoleEntity> userRoles = Arrays
                    .stream(UserRoleEnum.values())
                    .map(UserRoleEntity::new)
                    .toList();

            this.userRoleRepository.saveAll(userRoles);
        }
    }

    public UserRoleEntity getUserRole() {
        return this.userRoleRepository.findByUserRole(UserRoleEnum.USER).get();
    }

    public UserRoleEntity getModeratorRole() {
        return this.userRoleRepository.findByUserRole(UserRoleEnum.MODERATOR).get();
    }
}
