package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.model.dto.post.AddPostDTO;
import bg.softuni.kickboxing.model.dto.post.SearchPostDTO;
import bg.softuni.kickboxing.model.enums.PostCategoryEnum;
import bg.softuni.kickboxing.model.user.GlovesOnPointUserDetails;
import bg.softuni.kickboxing.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @ModelAttribute("searchPostModel")
    public SearchPostDTO initSearchPostModel() {
        return new SearchPostDTO();
    }

    @GetMapping("")
    public String forum(Model model,
                        @PageableDefault(size = 9) Pageable pageable) {
        model.addAttribute("posts", this.postService.getAllApprovedPostsOrderedByDateDesc(pageable));
        return "forum";
    }

    @GetMapping("/category/{category}")
    public String postsByCategory(Model model,
                                  @PathVariable("category") String category,
                                  @PageableDefault(size = 9) Pageable pageable) {

        model.addAttribute("posts", this.postService.getAllApprovedPostsByCategoryOrderedByCreatedOnDesc(pageable, category));
        return "forum";
    }

    @GetMapping("/search")
    public String search(Model model,
                         @Valid SearchPostDTO searchPostModel,
                         @RequestParam("query") String query,
                         @PageableDefault(size = 9) Pageable pageable) {

        if (!searchPostModel.isEmpty()) {
            model.addAttribute("posts", this.postService.getAllApprovedPostsWhereTitleLike(pageable, query));
        }

        return "forum";
    }

    @GetMapping("/posts/add")
    public String addPost() {
        return "post-add";
    }

    @PostMapping("/posts/add")
    public String addPost(@Valid AddPostDTO addPostModel,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          @AuthenticationPrincipal GlovesOnPointUserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addPostModel", addPostModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addPostModel", bindingResult);

            return "redirect:/forum/posts/add";
        }

        this.postService.addPost(addPostModel, userDetails);

        if (userDetails.getRole().equals("USER")) {
            redirectAttributes.addFlashAttribute("success", "Your post will be reviewed by a moderator.");
        }

        return "redirect:/forum/posts/add";
    }

    @GetMapping("/posts/{id}")
    public String post(@PathVariable("id") Long id, Model model) {
        model.addAttribute("post", this.postService.getPostDetailsById(id));
        this.postService.increaseViewsCount(id);
        return "post-details";
    }
}
