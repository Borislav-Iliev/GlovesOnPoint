package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.model.dto.user.SearchUserDTO;
import bg.softuni.kickboxing.model.exception.UsernameNotFoundException;
import bg.softuni.kickboxing.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("searchUserModel")
    private SearchUserDTO initSearchUserModel() {
        return new SearchUserDTO();
    }

    @GetMapping("/moderators")
    public String moderators() {
        return "admin-moderators";
    }

    @PostMapping("/moderators")
    public String moderators(@Valid SearchUserDTO searchUserModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("searchUserModel", searchUserModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.searchUserModel", bindingResult);

            return "redirect:/admin/moderators";
        }

        return String.format("redirect:/admin/moderators/%s", searchUserModel.getQuery());
    }

    @GetMapping("/moderators/{query}")
    public String searchResults(@PathVariable String query, Model model) {
        model.addAttribute("user", this.userService.getUserDTOByUsername(query));
        return "admin-moderators";
    }

    @GetMapping("/moderators/make/{username}")
    public String makeModerator(@PathVariable("username") String username) {
        this.userService.makeModerator(username);
        return "redirect:/admin/moderators";
    }

    @GetMapping("/moderators/remove/{username}")
    public String removeModerator(@PathVariable("username") String username) {
        this.userService.removeModerator(username);
        return "redirect:/admin/moderators";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView onInvalidUsernamePathVariable(UsernameNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("username-not-found");
        modelAndView.addObject("username", e.getUsername());

        return modelAndView;
    }
}
