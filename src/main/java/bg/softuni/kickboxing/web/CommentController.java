package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.model.dto.comment.AddCommentDTO;
import bg.softuni.kickboxing.model.user.KickboxingUserDetails;
import bg.softuni.kickboxing.service.CommentService;
import bg.softuni.kickboxing.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @ModelAttribute("addCommentModel")
    public AddCommentDTO initAddCommentModel() {
        return new AddCommentDTO();
    }

    @GetMapping("/forum/posts/{id}/add/comment")
    public String addComment(@PathVariable("id") Long id, Model model) {
        model.addAttribute("post", this.postService.findById(id));
        return "comment-add";
    }

    @PostMapping("/forum/posts/{id}/add/comment")
    public String addComment(@PathVariable("id") Long id,
                             @Valid AddCommentDTO addCommentModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal KickboxingUserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addCommentModel", addCommentModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addCommentModel", bindingResult);

            return "redirect:/forum/posts/{id}/add/comment";
        }

        this.commentService.addComment(id, addCommentModel, userDetails);

        return "redirect:/forum/posts/{id}/add/comment";
    }

}
