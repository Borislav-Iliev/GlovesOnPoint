package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.model.dto.post.AddPostDTO;
import bg.softuni.kickboxing.model.user.KickboxingUserDetails;
import bg.softuni.kickboxing.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/forum")
public class ForumController {

    private final PostService postService;

    public ForumController(PostService postService) {
        this.postService = postService;
    }

    @ModelAttribute("addPostModel")
    public AddPostDTO initAddPostModel() {
        return new AddPostDTO();
    }

    @GetMapping("")
    public String forum(Model model) {
        model.addAttribute("posts", this.postService.getAllApprovedPostsOrderedByDateDesc());
        return "forum";
    }

    @GetMapping("/posts/add")
    public String addPost() {
        return "add-post";
    }

    @PostMapping("/posts/add")
    public String addNews(@Valid AddPostDTO addPostModel,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          @AuthenticationPrincipal KickboxingUserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addPostModel", addPostModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addPostModel", bindingResult);

            return "redirect:/forum/posts/add";
        }

        this.postService.addPost(addPostModel, userDetails);

        return "redirect:/forum";
    }

    @GetMapping("/posts/{id}")
    public String post(@PathVariable Long id, Model model) {
        model.addAttribute(this.postService.getPostById(id));
        this.postService.increaseViewsCountById(id);
        return "post-details";
    }
}
