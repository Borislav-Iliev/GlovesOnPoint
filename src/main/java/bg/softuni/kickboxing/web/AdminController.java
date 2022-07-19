package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PostService postService;

    public AdminController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    public String admin() {
        return "admin";
    }

    @GetMapping("/posts")
    public String posts(Model model,
                        @PageableDefault(
                                page = 0,
                                size = 9) Pageable pageable) {
        model.addAttribute("posts", this.postService.getAllNotApprovedPostsOrderedByDateDesc(pageable));
        return "admin-posts";
    }

    @GetMapping("/posts/approve/{id}")
    public String approvePost(@PathVariable Long id) {
        this.postService.approvePost(id);
        return "redirect:/admin/posts";
    }

    @GetMapping("/posts/disapprove/{id}")
    public String disapprovePost(@PathVariable Long id) {
        this.postService.disapprovePost(id);
        return "redirect:/admin/posts";
    }
}
