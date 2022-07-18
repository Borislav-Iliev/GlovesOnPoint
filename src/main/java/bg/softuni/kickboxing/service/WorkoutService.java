package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.dto.workout.AddWorkoutDTO;
import bg.softuni.kickboxing.model.dto.workout.WorkoutInformationDTO;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.WorkoutEntity;
import bg.softuni.kickboxing.model.enums.WorkoutLevelEnum;
import bg.softuni.kickboxing.repository.UserRepository;
import bg.softuni.kickboxing.repository.WorkoutRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<WorkoutInformationDTO> getAllWorkouts() {
        return this.workoutRepository
                .findAll()
                .stream()
                .map(w -> this.mapper.map(w, WorkoutInformationDTO.class))
                .toList();
    }

    public List<WorkoutInformationDTO> getAllWorkoutsByLevel(WorkoutLevelEnum level) {
        return this.workoutRepository.findAllByLevel(level);
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
        this.workoutRepository.deleteById(id);
    }
}
