package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.model.dto.user.EditUserDTO;
import bg.softuni.kickboxing.model.dto.user.UserRegistrationDTO;
import bg.softuni.kickboxing.service.UserService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("userRegistrationModel")
    public UserRegistrationDTO initUserRegistrationModel() {
        return new UserRegistrationDTO();
    }

    @ModelAttribute("editUserModel")
    public EditUserDTO initEditUserModel() {
        return new EditUserDTO();
    }

    @GetMapping("/register")
    public String register() {
        return "auth-register";
    }

    @PostMapping("/register")
    public String register(@Valid UserRegistrationDTO userRegistrationModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegistrationModel", userRegistrationModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationModel", bindingResult);

            return "redirect:/users/register";
        }

        this.userService.registerAndLogin(userRegistrationModel);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "auth-login";
    }

    @PostMapping("/login-error")
    public String loginError(@ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
                             RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        redirectAttributes.addFlashAttribute("badCredentials", true);

        return "redirect:/users/login";
    }

    @GetMapping("/profile/{id}")
    public String profile(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", this.userService.getUserDTOById(id));
        return "profile";
    }

    @GetMapping("/profile/edit/{id}")
    public String editProfile(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", this.userService.getUserDTOById(id));
        return "profile-edit";
    }

    @PostMapping("/profile/edit/{id}")
    public String editProfile(@PathVariable("id") Long id,
                              @Valid EditUserDTO editUserModel,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("editUserModel", editUserModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editUserModel", bindingResult);

            return "redirect:/users/profile/edit/{id}";
        }

        redirectAttributes.addFlashAttribute("success", "Your have successfully updated your profile!");
        this.userService.editProfile(editUserModel, id);

        return "redirect:/users/profile/edit/{id}";
    }
}
