package bg.softuni.kickboxing.web;

import bg.softuni.kickboxing.model.dto.news.AddNewsDTO;
import bg.softuni.kickboxing.service.NewsService;
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
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService postService) {
        this.newsService = postService;
    }

    @ModelAttribute("addNewsModel")
    public AddNewsDTO initAddNewsModel() {
        return new AddNewsDTO();
    }

    @GetMapping("")
    public String news(Model model,
                       @PageableDefault(size = 4) Pageable pageable) {
        model.addAttribute("news", this.newsService.getAllNewsOrderedByDateDesc(pageable));
        model.addAttribute("trending", this.newsService.getAllTrendingNewsOrderedByDateDesc());
        return "news";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        model.addAttribute("news", this.newsService.getNewsById(id));
        this.newsService.increaseViewsCount(id);
        return "news-details";
    }

    @GetMapping("/add")
    public String addNews() {
        return "news-add";
    }

    @PostMapping("/add")
    public String addNews(@Valid AddNewsDTO addNewsModel,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          @AuthenticationPrincipal UserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addNewsModel", addNewsModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addNewsModel", bindingResult);

            return "redirect:/news/add";
        }

        this.newsService.addNews(addNewsModel, userDetails);

        return "redirect:/news";
    }

    @GetMapping("/delete/{id}")
    public String deleteNews(@PathVariable Long id) {
        this.newsService.deleteNews(id);
        return "redirect:/news";
    }
}
