package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.entity.WorkoutEntity;
import bg.softuni.kickboxing.model.enums.WorkoutLevelEnum;
import bg.softuni.kickboxing.model.enums.WorkoutTypeEnum;
import bg.softuni.kickboxing.repository.UserRepository;
import bg.softuni.kickboxing.repository.UserRoleRepository;
import bg.softuni.kickboxing.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkoutControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    private UserEntity userEntity;

    private WorkoutEntity workoutEntity;

    @BeforeEach
    public void setUp() {
        this.userEntity = initUser(this.userRoleRepository.findAll().get(2));
        this.workoutEntity = initWorkout();
    }

    public UserEntity initUser(UserRoleEntity userRole) {
        UserEntity testUserEntity = new UserEntity()
                .setUsername("User")
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("test2@example.com")
                .setPassword("testPassword")
                .setAge(20)
                .setImageUrl("image:/url")
                .setUserRoles(List.of(userRole));
        testUserEntity.setId(1L);

        this.userRepository.save(testUserEntity);
        return testUserEntity;
    }

    public WorkoutEntity initWorkout() {
        WorkoutEntity workoutEntity = new WorkoutEntity()
                .setTitle("Title")
                .setLevel(WorkoutLevelEnum.HARD)
                .setType(WorkoutTypeEnum.STRENGTH)
                .setContent("Valid content!")
                .setImageUrl("image:/url");
        workoutEntity.setId(1L);

        this.workoutRepository.save(workoutEntity);
        return workoutEntity;
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    public void testWorkoutsPageShownForAuthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/workouts"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("workouts"))
                .andExpect(view().name("workouts"));
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    public void testEasyWorkoutsPageShownForAuthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/workouts/easy"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("workouts"))
                .andExpect(view().name("workouts-easy"));
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    public void testIntermediateWorkoutsPageShownForAuthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/workouts/intermediate"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("workouts"))
                .andExpect(view().name("workouts-intermediate"));
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    public void testHardWorkoutsPageShownForAuthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/workouts/hard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("workouts"))
                .andExpect(view().name("workouts-hard"));
    }

    @Test
    @WithMockUser(username = "User", roles = "MODERATOR")
    public void testAddWorkoutPageShownForAuthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/workouts/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("workout-add"));
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    public void testAddWorkoutWithNormalUser_AccessForbidden() throws Exception {
        this.mockMvc.perform(get("/workouts/add"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "User", roles = "MODERATOR")
    public void testAddWorkoutWithModeratorUser_ValidData() throws Exception {
        this.mockMvc.perform(post("/workouts/add")
                        .param("title", "Some Title!")
                        .param("type", "STRENGTH")
                        .param("level", "HARD")
                        .param("imageUrl", "https://www.example.com/")
                        .param("content", "Some valid content!")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/workouts"));
    }

    @Test
    @WithMockUser(username = "User", roles = "MODERATOR")
    public void testAddWorkoutWithModeratorUser_InvalidData() throws Exception {
        this.mockMvc.perform(post("/workouts/add")
                        .param("title", "Some Title!")
                        .param("type", "")
                        .param("level", "")
                        .param("imageUrl", "https://www.example.com/")
                        .param("content", "Some valid content!")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("addWorkoutModel", "org.springframework.validation.BindingResult.addWorkoutModel"))
                .andExpect(redirectedUrl("/workouts/add"));
    }

    @Test
    @WithMockUser(username = "User", roles = "MODERATOR")
    public void testDeleteWorkoutPageWithModerator() throws Exception {
        this.mockMvc.perform(get("/workouts/delete/{id}", this.workoutEntity.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/workouts"));
    }

    @Test
    @WithMockUser(username = "User", roles = "User")
    public void testWorkoutDetailsPageShownForAuthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/workouts/details/{id}", this.workoutEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("workout"))
                .andExpect(view().name("workout-details"));
    }
}
