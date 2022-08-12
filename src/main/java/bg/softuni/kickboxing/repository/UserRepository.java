package bg.softuni.kickboxing.repository;

import bg.softuni.kickboxing.model.dto.user.TopUserDTO;
import bg.softuni.kickboxing.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT new bg.softuni.kickboxing.model.dto.user.TopUserDTO " +
            "(u.username, u.firstName, u.lastName, u.posts.size, u.comments.size)" +
            " FROM UserEntity u" +
            " ORDER BY u.posts.size DESC")
    List<TopUserDTO> getAllDistinctByOrderByPostsDesc();
}
