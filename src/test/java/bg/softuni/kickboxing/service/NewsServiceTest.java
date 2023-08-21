package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.dto.comment.CommentDTO;
import bg.softuni.kickboxing.model.dto.news.AddNewsDTO;
import bg.softuni.kickboxing.model.dto.news.NewsDTO;
import bg.softuni.kickboxing.model.entity.NewsEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.UserRoleEnum;
import bg.softuni.kickboxing.repository.NewsRepository;
import bg.softuni.kickboxing.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {

    @InjectMocks
    private NewsService newsService;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private UserDetails userDetails;

    private Pageable pageable;

    @Test
    void testAddNews_ShouldAddNews_WhenCorrectNews() {
        AddNewsDTO addNewsDTO = initAddNewsDto();
        NewsEntity news = initNews();
        when(this.mapper.map(addNewsDTO, NewsEntity.class))
                .thenReturn(news);

        UserEntity user = initUser();
        when(this.userDetails.getUsername())
                .thenReturn("Username");
        when(this.userRepository.findByUsername(this.userDetails.getUsername()))
                .thenReturn(Optional.of(user));

        when(this.newsRepository.save(news))
                .thenReturn(news);

        this.newsService.addNews(addNewsDTO, this.userDetails);

        verify(this.newsRepository, times(1)).save(news);
    }

    @Test
    void testAddNews_ShouldThrowException_WhenIncorrectAuthor() {
        AddNewsDTO addNewsDTO = initAddNewsDto();
        NewsEntity news = initNews();
        when(this.mapper.map(addNewsDTO, NewsEntity.class))
                .thenReturn(news);

        when(this.userDetails.getUsername())
                .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> this.newsService.addNews(addNewsDTO, this.userDetails));
    }

    @Test
    void testGetAllNewsOrderedByDateDesc_ShouldReturnAListOfAllNewsDto() {
        List<NewsDTO> news = List.of(initNewsDto(), initNewsDto());
        PageImpl<NewsDTO> expectedNewsPage = new PageImpl<>(news);

        when(this.newsRepository.findAllByOrderByCreatedOnDescIdDesc(this.pageable))
                .thenReturn(expectedNewsPage);

        Page<NewsDTO> actualNewsPage = this.newsService.getAllNewsOrderedByDateDesc(this.pageable);

        assertIterableEquals(expectedNewsPage, actualNewsPage);
    }

    private NewsEntity initNews() {
        return new NewsEntity()
                .setTitle("Title")
                .setContent("Content")
                .setCreatedOn(LocalDate.now())
                .setViews(1)
                .setImageUrl("image:/url")
                .setAuthor(initUser());
    }

    private UserEntity initUser() {
        return new UserEntity()
                .setUsername("TestUsername")
                .setFirstName("Test")
                .setLastName("Test")
                .setEmail("test@example.com")
                .setPassword("testPassword")
                .setAge(20)
                .setImageUrl("image:/url")
                .setUserRoles(List.of(new UserRoleEntity(UserRoleEnum.ADMIN),
                        new UserRoleEntity(UserRoleEnum.MODERATOR),
                        new UserRoleEntity(UserRoleEnum.USER)));
    }

    private AddNewsDTO initAddNewsDto() {
        AddNewsDTO addNewsDTO = new AddNewsDTO();
        addNewsDTO.setContent("Content");
        addNewsDTO.setTitle("Title");
        addNewsDTO.setImageUrl("image:/url");
        return addNewsDTO;
    }

    private NewsDTO initNewsDto() {
        return new NewsDTO(
                1L, "Title", "Content", "image:/url", 1, LocalDate.now()
        );
    }
}
