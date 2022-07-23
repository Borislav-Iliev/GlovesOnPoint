package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.model.dto.workout.AddWorkoutDTO;
import bg.softuni.kickboxing.model.enums.WorkoutLevelEnum;
import bg.softuni.kickboxing.service.WorkoutService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @ModelAttribute("addWorkoutModel")
    public AddWorkoutDTO initAddWorkoutModel() {
        return new AddWorkoutDTO();
    }

    @GetMapping("")
    public String workouts(Model model,
                           @PageableDefault(
                                   page = 0,
                                   size = 8) Pageable pageable) {
        model.addAttribute("workouts", this.workoutService.getAllWorkoutsOrderedByWorkoutLevel(pageable));
        return "workouts";
    }

    @GetMapping("/easy")
    public String easyWorkouts(Model model,
                               @PageableDefault(
                                       page = 0,
                                       size = 8) Pageable pageable) {
        model.addAttribute("workouts", this.workoutService.getAllWorkoutsByLevel(WorkoutLevelEnum.EASY, pageable));
        return "workouts-easy";
    }

    @GetMapping("/intermediate")
    public String intermediateWorkouts(Model model,
                                       @PageableDefault(
                                               page = 0,
                                               size = 8) Pageable pageable) {
        model.addAttribute("workouts", this.workoutService.getAllWorkoutsByLevel(WorkoutLevelEnum.INTERMEDIATE, pageable));
        return "workouts-intermediate";
    }

    @GetMapping("/hard")
    public String hardWorkouts(Model model,
                               @PageableDefault(
                                       page = 0,
                                       size = 8) Pageable pageable) {
        model.addAttribute("workouts", this.workoutService.getAllWorkoutsByLevel(WorkoutLevelEnum.HARD, pageable));
        return "workouts-hard";
    }

    @GetMapping("/add")
    public String addWorkout() {
        return "workout-add";
    }

    @PostMapping("/add")
    public String register(@Valid AddWorkoutDTO addWorkoutModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal UserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addWorkoutModel", addWorkoutModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addWorkoutModel", bindingResult);

            return "redirect:/workouts/add";
        }

        this.workoutService.addWorkout(addWorkoutModel, userDetails);

        return "redirect:/workouts";
    }

    @GetMapping("/delete/{id}")
    public String deleteWorkout(@PathVariable Long id) {
        this.workoutService.deleteWorkout(id);
        return "redirect:/workouts";
    }

    @GetMapping("/details/{id}")
    public String workoutDetails(@PathVariable("id") Long id, Model model) {
        model.addAttribute("workout", this.workoutService.getWorkoutById(id));
        return "workout-details";
    }
}