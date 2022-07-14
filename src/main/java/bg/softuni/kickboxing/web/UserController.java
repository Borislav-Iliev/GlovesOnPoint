package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.model.dto.user.UserRegistrationDTO;
import bg.softuni.kickboxing.service.UserService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }
}
