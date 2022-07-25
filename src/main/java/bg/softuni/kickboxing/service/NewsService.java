package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.dto.news.AddNewsDTO;
import bg.softuni.kickboxing.model.dto.news.NewsDTO;
import bg.softuni.kickboxing.model.entity.NewsEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.exception.ObjectNotFoundException;
import bg.softuni.kickboxing.repository.NewsRepository;
import bg.softuni.kickboxing.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public NewsService(NewsRepository newsRepository, UserRepository userRepository, ModelMapper mapper) {
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public void addNews(AddNewsDTO addNewsDto, UserDetails userDetails) {
        NewsEntity news = this.mapper.map(addNewsDto, NewsEntity.class);

        news.setCreatedOn(LocalDate.now());

        UserEntity author = this.userRepository
                .findByUsername(userDetails.getUsername())
                .orElseThrow();
        news.setAuthor(author);

        this.newsRepository.save(news);
    }

    public Page<NewsDTO> getAllNewsOrderedByDateDesc(Pageable pageable) {
        return this.newsRepository.findAllByOrderByCreatedOnDescIdDesc(pageable);
    }

    public List<NewsDTO> getAllTrendingNewsOrderedByDateDesc() {
        return this.newsRepository
                .findTrendingNewsOrderByCreatedOnDescIdDesc()
                .stream()
                .limit(3)
                .collect(Collectors.toList());
    }

    public NewsDTO getNewsById(Long id) {
        return this.newsRepository
                .findById(id)
                .map(n -> this.mapper.map(n, NewsDTO.class))
                .orElseThrow(() -> new ObjectNotFoundException(id));
    }

    public void deleteNews(Long id) {
        if (id > this.getIdOfLastObjectInTable()) {
            throw new ObjectNotFoundException(id);
        }
        this.newsRepository.deleteById(id);
    }

    public Long getIdOfLastObjectInTable() {
        return this.newsRepository.findTopByOrderByIdDesc().getId();
    }

    public void increaseViewsCount(Long id) {
        NewsEntity news = this.newsRepository
                .findById(id)
                .orElseThrow();
        news.setViews(news.getViews() + 1);
        this.newsRepository.save(news);
    }
}
