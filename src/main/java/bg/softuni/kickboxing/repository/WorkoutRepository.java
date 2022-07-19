package bg.softuni.kickboxing.repository;

import bg.softuni.kickboxing.model.dto.workout.WorkoutDTO;
import bg.softuni.kickboxing.model.entity.WorkoutEntity;
import bg.softuni.kickboxing.model.enums.WorkoutLevelEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRepository extends JpaRepository<WorkoutEntity, Long> {

    @Query("SELECT new bg.softuni.kickboxing.model.dto.workout.WorkoutDTO" +
            " (w.id, w.level, w.content, w.imageUrl)" +
            " FROM WorkoutEntity w")
    Page<WorkoutDTO> findAllWorkouts(Pageable pageable);

    @Query("SELECT new bg.softuni.kickboxing.model.dto.workout.WorkoutDTO" +
            " (w.id, w.level, w.content, w.imageUrl)" +
            " FROM WorkoutEntity w" +
            " WHERE w.level = :level")
    Page<WorkoutDTO> findAllByLevel(WorkoutLevelEnum level, Pageable pageable);
}
