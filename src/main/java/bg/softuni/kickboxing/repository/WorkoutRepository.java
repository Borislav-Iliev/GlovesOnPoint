package bg.softuni.kickboxing.repository;

import bg.softuni.kickboxing.model.entity.WorkoutEntity;
import bg.softuni.kickboxing.model.enums.WorkoutLevelEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<WorkoutEntity, Long> {
    List<WorkoutEntity> findAllByLevel(WorkoutLevelEnum level);
}
