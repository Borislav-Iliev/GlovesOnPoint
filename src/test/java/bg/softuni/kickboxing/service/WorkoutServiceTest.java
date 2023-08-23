package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.dto.workout.AddWorkoutDTO;
import bg.softuni.kickboxing.model.dto.workout.WorkoutDTO;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.entity.WorkoutEntity;
import bg.softuni.kickboxing.model.enums.UserRoleEnum;
import bg.softuni.kickboxing.model.enums.WorkoutLevelEnum;
import bg.softuni.kickboxing.model.enums.WorkoutTypeEnum;
import bg.softuni.kickboxing.model.exception.ObjectNotFoundException;
import bg.softuni.kickboxing.model.user.GlovesOnPointUserDetails;
import bg.softuni.kickboxing.repository.UserRepository;
import bg.softuni.kickboxing.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest {

    @InjectMocks
    private WorkoutService workoutService;

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private Pageable pageable;

    @Mock
    private GlovesOnPointUserDetails userDetails;

    static List<WorkoutDTO> workouts;
    static Page<WorkoutDTO> expectedWorkoutsPage;

    static WorkoutEntity workout;
    static WorkoutDTO workoutDto;
    static AddWorkoutDTO addWorkoutDto;
    static UserEntity user;
    @BeforeAll
    static void setUp() {
        workouts = List.of(initWorkoutDto(), initWorkoutDto());
        expectedWorkoutsPage = new PageImpl<>(workouts);
        workout = initWorkout();
        workoutDto = initWorkoutDto();
        addWorkoutDto = initAddWorkoutDto();
        user = initUser();
    }

    @Test
    void testGetAllWorkoutsOrderedByWorkoutLevel_ShouldReturnCorrectPageOfWorkouts() {
        when(this.workoutRepository.findAllWorkoutsOrderedByWorkoutLevel(this.pageable))
                .thenReturn(expectedWorkoutsPage);

        Page<WorkoutDTO> actualWorkoutsPage = this.workoutService
                .getAllWorkoutsOrderedByWorkoutLevel(this.pageable);

        assertIterableEquals(expectedWorkoutsPage, actualWorkoutsPage);
    }

    @Test
    void testGetAllWorkoutsByLevel_ShouldReturnCorrectPageOfWorkouts() {
        when(this.workoutRepository.findAllByLevel(WorkoutLevelEnum.EASY, this.pageable))
                .thenReturn(expectedWorkoutsPage);

        Page<WorkoutDTO> actualWorkoutsPage = this.workoutService
                .getAllWorkoutsByLevel(WorkoutLevelEnum.EASY, this.pageable);

        assertIterableEquals(expectedWorkoutsPage, actualWorkoutsPage);
    }

    @Test
    void testAddWorkout_ShouldAddWorkout_WhenValidDataIsPassed() {
        when(this.mapper.map(addWorkoutDto, WorkoutEntity.class))
                .thenReturn(workout);

        when(this.userDetails.getUsername())
                .thenReturn("Username");
        when(this.userRepository.findByUsername(this.userDetails.getUsername()))
                .thenReturn(Optional.of(user));

        this.workoutService.addWorkout(addWorkoutDto, this.userDetails);

        verify(this.workoutRepository, times(1)).save(workout);
    }

    @ParameterizedTest
    @CsvSource(value = {"1", "2", "3", "4"})
    void testDeleteWorkout_ShouldDeleteWorkout_WhenValidWorkoutIdIsPassed(Long id) {
        workout.setId(9L);
        when(this.workoutRepository.findTopByOrderByIdDesc())
                .thenReturn(workout);

        this.workoutService.deleteWorkout(id);

        verify(this.workoutRepository, times(1)).deleteById(id);
    }

    @ParameterizedTest
    @CsvSource(value = {"2", "3", "4"})
    void testDeleteWorkout_ShouldThrowException_WhenInvalidWorkoutIdIsPassed(Long id) {
        workout.setId(1L);
        when(this.workoutRepository.findTopByOrderByIdDesc())
                .thenReturn(workout);

        Executable executable = () -> this.workoutService.deleteWorkout(id);

        assertThrows(ObjectNotFoundException.class, executable);
    }

    @Test
    void testGetIdOfLastObjectInTable_ShouldReturnValidIdOfLastObject() {
        workout.setId(1L);
        when(this.workoutRepository.findTopByOrderByIdDesc())
                .thenReturn(workout);

        Long actualLastId = this.workoutService.getIdOfLastObjectInTable();

        assertEquals(workout.getId(), actualLastId);
    }

    @ParameterizedTest
    @CsvSource(value = {"1", "2", "3", "4"})
    void testGetWorkoutById_ShouldReturnCorrectWorkout_WhenValidWorkoutIdIsPassed(Long id) {
        when(this.workoutRepository.findById(id))
                .thenReturn(Optional.of(workout));

        when(this.mapper.map(workout, WorkoutDTO.class))
                .thenReturn(workoutDto);

        WorkoutDTO actualWorkoutDto = this.workoutService.getWorkoutById(id);

        assertEquals(workoutDto.getId(), actualWorkoutDto.getId());
        assertEquals(workoutDto.getTitle(), actualWorkoutDto.getTitle());
        assertEquals(workoutDto.getLevel(), actualWorkoutDto.getLevel());
        assertEquals(workoutDto.getType(), actualWorkoutDto.getType());
        assertEquals(workoutDto.getContent(), actualWorkoutDto.getContent());
        assertEquals(workoutDto.getImageUrl(), actualWorkoutDto.getImageUrl());
    }

    @ParameterizedTest
    @CsvSource(value = {"-1", "-2", "-3", "-4"})
    void testGetWorkoutById_ShouldThrowException_WhenInvalidWorkoutIdIsPassed(Long id) {
        Executable executable = () -> this.workoutService.getWorkoutById(id);

        assertThrows(ObjectNotFoundException.class, executable);
    }

    private static WorkoutEntity initWorkout() {
        return new WorkoutEntity()
                .setTitle("Title")
                .setLevel(WorkoutLevelEnum.EASY)
                .setType(WorkoutTypeEnum.CARDIO)
                .setContent("Content")
                .setImageUrl("image:/url")
                .setAuthor(initUser());
    }

    private static UserEntity initUser() {
        return new UserEntity()
                .setUsername("TestUsername")
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("test@example.com")
                .setPassword("testPassword")
                .setAge(20)
                .setImageUrl("image:/url")
                .setUserRoles(List.of(new UserRoleEntity(UserRoleEnum.ADMIN),
                        new UserRoleEntity(UserRoleEnum.MODERATOR),
                        new UserRoleEntity(UserRoleEnum.USER)));
    }

    private static WorkoutDTO initWorkoutDto() {
        return new WorkoutDTO(
                1L, "Title", WorkoutLevelEnum.EASY, WorkoutTypeEnum.CARDIO, "Content", "image:/url"
        );
    }

    public static AddWorkoutDTO initAddWorkoutDto() {
        AddWorkoutDTO addWorkoutDto = new AddWorkoutDTO();
        addWorkoutDto.setTitle("Title");
        addWorkoutDto.setLevel(WorkoutLevelEnum.EASY);
        addWorkoutDto.setType(WorkoutTypeEnum.CARDIO);
        addWorkoutDto.setContent("Content");
        addWorkoutDto.setImageUrl("image:/url");
        return addWorkoutDto;
    }
}
