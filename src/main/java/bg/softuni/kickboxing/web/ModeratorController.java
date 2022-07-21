package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.service.CommentService;
import bg.softuni.kickboxing.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/moderator")
public class ModeratorController {

    private final PostService postService;
    private final CommentService commentService;

    public ModeratorController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("")
    public String moderator() {
        return "moderator";
    }

    @GetMapping("/posts")
    public String posts(Model model,
                        @PageableDefault(size = 9) Pageable pageable) {
        model.addAttribute("posts", this.postService.getAllNotApprovedPostsOrderedByDateDesc(pageable));
        return "moderator-posts";
    }

    @GetMapping("/posts/approve/{id}")
    public String approvePost(@PathVariable("id") Long id) {
        this.postService.approvePost(id);
        return "redirect:/moderator/posts";
    }

    @GetMapping("/posts/disapprove/{id}")
    public String disapprovePost(@PathVariable("id") Long id) {
        this.postService.disapprovePost(id);
        return "redirect:/moderator/posts";
    }

    @GetMapping("/comments")
    public String comments(Model model,
                           @PageableDefault(size = 9) Pageable pageable) {
        model.addAttribute("comments", this.commentService.getAllNotApprovedCommentsOrderedByDateDesc(pageable));
        return "moderator-comments";
    }

    @GetMapping("/comments/approve/{id}")
    public String approveComment(@PathVariable("id") Long id) {
        this.commentService.approveComment(id);
        return "redirect:/moderator/comments";
    }

    @GetMapping("/comments/disapprove/{id}")
    public String disapproveComment(@PathVariable("id") Long id) {
        this.commentService.disapproveComment(id);
        return "redirect:/moderator/comments";
    }
}
