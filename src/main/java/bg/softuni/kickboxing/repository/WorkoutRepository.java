package bg.softuni.kickboxing.repository;

import bg.softuni.kickboxing.model.dto.workout.WorkoutInformationDTO;
import bg.softuni.kickboxing.model.entity.WorkoutEntity;
import bg.softuni.kickboxing.model.enums.WorkoutLevelEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<WorkoutEntity, Long> {

    @Query("SELECT new bg.softuni.kickboxing.model.dto.workout.WorkoutInformationDTO" +
            " (w.id, w.level, w.content, w.imageUrl)" +
            " FROM WorkoutEntity w" +
            " WHERE w.level = :level")
    List<WorkoutInformationDTO> findAllByLevel(WorkoutLevelEnum level);
}
