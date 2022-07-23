package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.dto.workout.AddWorkoutDTO;
import bg.softuni.kickboxing.model.dto.workout.WorkoutDTO;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.WorkoutEntity;
import bg.softuni.kickboxing.model.enums.WorkoutLevelEnum;
import bg.softuni.kickboxing.model.exception.ObjectNotFoundException;
import bg.softuni.kickboxing.repository.UserRepository;
import bg.softuni.kickboxing.repository.WorkoutRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository, ModelMapper mapper) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public Page<WorkoutDTO> getAllWorkoutsOrderedByWorkoutLevel(Pageable pageable) {
        return this.workoutRepository.findAllWorkoutsOrderedByWorkoutLevel(pageable);
    }

    public Page<WorkoutDTO> getAllWorkoutsByLevel(WorkoutLevelEnum level, Pageable pageable) {
        return this.workoutRepository.findAllByLevel(level, pageable);
    }

    public void addWorkout(AddWorkoutDTO addWorkoutDTO, UserDetails userDetails) {
        WorkoutEntity workout = this.mapper.map(addWorkoutDTO, WorkoutEntity.class);

        UserEntity author = this.userRepository
                .findByUsername(userDetails.getUsername())
                .orElseThrow();
        workout.setAuthor(author);

        this.workoutRepository.save(workout);
    }

    public void deleteWorkout(Long id) {
        if (id > this.getIdOfLastObjectInTable()) {
            throw new ObjectNotFoundException(id);
        }
        this.workoutRepository.deleteById(id);
    }

    public Long getIdOfLastObjectInTable() {
        return this.workoutRepository.findTopByOrderByIdDesc().getId();
    }

    public WorkoutDTO getWorkoutById(Long id) {
        return this.workoutRepository
                .findById(id)
                .map(w -> this.mapper.map(w, WorkoutDTO.class))
                .orElseThrow(() -> new ObjectNotFoundException(id));
    }
}
