package bg.softuni.kickboxing.repository;

import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    Optional<UserRoleEntity> findByUserRole(UserRoleEnum userRole);
}
