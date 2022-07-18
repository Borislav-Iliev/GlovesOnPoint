package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.dto.news.AddNewsDTO;
import bg.softuni.kickboxing.model.dto.news.NewsInformationDTO;
import bg.softuni.kickboxing.model.entity.NewsEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.repository.NewsRepository;
import bg.softuni.kickboxing.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    public List<NewsInformationDTO> getAllNewsOrderedByDateDesc() {
        return this.newsRepository.findAllByOrderByDateDescIdDesc();
    }

    public NewsEntity getNewsById(Long id) {
        return this.newsRepository
                .findById(id)
                .orElse(null);
    }

    public void addNews(AddNewsDTO addNewsDto, UserDetails userDetails) {
        NewsEntity news = this.mapper.map(addNewsDto, NewsEntity.class);

        news.setDate(LocalDate.now());

        UserEntity author = this.userRepository
                .findByUsername(userDetails.getUsername())
                .orElseThrow();
        news.setAuthor(author);

        this.newsRepository.save(news);
    }

    public void deleteNews(Long id) {
        this.newsRepository.deleteById(id);
    }
}
