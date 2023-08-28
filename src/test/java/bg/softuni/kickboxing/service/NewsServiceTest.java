package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.dto.news.AddNewsDTO;
import bg.softuni.kickboxing.model.dto.news.NewsDTO;
import bg.softuni.kickboxing.model.entity.NewsEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.entity.UserRoleEntity;
import bg.softuni.kickboxing.model.enums.UserRoleEnum;
import bg.softuni.kickboxing.model.exception.ObjectNotFoundException;
import bg.softuni.kickboxing.repository.NewsRepository;
import bg.softuni.kickboxing.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

import static org.junit.jupiter.api.Assertions.*;
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

    @Mock
    private Pageable pageable;

    private static NewsEntity news;
    private static AddNewsDTO addNewsDto;
    private static NewsDTO newsDto;
    private static List<NewsDTO> newsDtoList;
    private static PageImpl<NewsDTO> expectedNewsPage;
    private static List<NewsEntity> newsEntityList;

    @BeforeAll
    static void setUp() {
        news = initNews();
        addNewsDto = initAddNewsDto();
        newsDto = initNewsDto();
        newsDtoList = List.of(initNewsDto(), initNewsDto());
        expectedNewsPage = new PageImpl<>(newsDtoList);
        newsEntityList = List.of(initNews(), initNews());
    }

    @Test
    void testAddNews_ShouldAddNews_WhenCorrectNews() {
        when(this.mapper.map(addNewsDto, NewsEntity.class))
                .thenReturn(news);

        UserEntity user = initUser();
        when(this.userDetails.getUsername())
                .thenReturn("Username");
        when(this.userRepository.findByUsername(this.userDetails.getUsername()))
                .thenReturn(Optional.of(user));

        when(this.newsRepository.save(news))
                .thenReturn(news);

        this.newsService.addNews(addNewsDto, this.userDetails);

        verify(this.newsRepository, times(1)).save(news);
    }

    @Test
    void testAddNews_ShouldThrowException_WhenIncorrectAuthor() {
        when(this.mapper.map(addNewsDto, NewsEntity.class))
                .thenReturn(news);

        when(this.userDetails.getUsername())
                .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> this.newsService.addNews(addNewsDto, this.userDetails));
    }

    @Test
    void testGetAllNewsOrderedByDateDesc_ShouldReturnAListOfAllNewsDto() {
        when(this.newsRepository.findAllByOrderByCreatedOnDescIdDesc(this.pageable))
                .thenReturn(expectedNewsPage);

        Page<NewsDTO> actualNewsPage = this.newsService.getAllNewsOrderedByDateDesc(this.pageable);

        assertIterableEquals(expectedNewsPage, actualNewsPage);
    }

    @Test
    void testGetAllTrendingNewsOrderedByDateDesc_ShouldReturnCorrectNews() {
        when(this.newsRepository.findTrendingNewsOrderByCreatedOnDescIdDesc())
                .thenReturn(newsDtoList);

        List<NewsDTO> actualNews = this.newsService.getAllTrendingNewsOrderedByDateDesc();

        assertIterableEquals(newsDtoList, actualNews);
    }

    @ParameterizedTest
    @CsvSource(value = {"1", "2", "3", "4"})
    void testGetNewsById_ShouldReturnCorrectNews_WhenNewsIdIsValid(Long id) {
        when(this.newsRepository.findById(id))
                .thenReturn(Optional.of(news));

        when(this.mapper.map(news, NewsDTO.class))
                .thenReturn(newsDto);

        NewsDTO newsById = this.newsService.getNewsById(id);

        assertAll(
                () -> assertEquals(newsDto.getId(), newsById.getId()),
                () -> assertEquals(newsDto.getTitle(), newsById.getTitle()),
                () -> assertEquals(newsDto.getContent(), newsById.getContent()),
                () -> assertEquals(newsDto.getImageUrl(), newsById.getImageUrl()),
                () -> assertEquals(newsDto.getViews(), newsById.getViews()),
                () -> assertEquals(newsDto.getCreatedOn(), newsById.getCreatedOn())
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"-1", "-2", "-3", "-4"})
    void testGetNewsById_ShouldThrowException_WhenNewsIdIsInvalid(Long id) {
        Executable executable = () -> this.newsService.getNewsById(id);

        assertThrows(ObjectNotFoundException.class, executable);
    }

    @ParameterizedTest
    @CsvSource(value = {"1", "2", "3", "4"})
    void testDeleteNews_ShouldDeleteNews(Long id) {
        news.setId(id);
        when(this.newsRepository.findTopByOrderByIdDesc())
                .thenReturn(news);

        this.newsService.deleteNews(id);

        verify(this.newsRepository, times(1)).deleteById(id);
    }

    @ParameterizedTest
    @CsvSource(value = {"2", "3", "4"})
    void testDeleteNews_ShouldThrowException_WhenNewsIdIsOutOfBounds(Long id) {
        news.setId(1L);
        when(this.newsRepository.findTopByOrderByIdDesc())
                .thenReturn(news);

        Executable executable = () -> this.newsService.deleteNews(id);

        assertThrows(ObjectNotFoundException.class, executable);
    }

    @Test
    void testGetIdOfLastObjectInTable_ShouldReturnIdOfLastObject() {
        news.setId(1L);
        when(this.newsRepository.findTopByOrderByIdDesc())
                .thenReturn(news);

        Long actualLastId = this.newsService.getIdOfLastObjectInTable();

        assertEquals(news.getId(), actualLastId);
    }

    @ParameterizedTest
    @CsvSource(value = {"1", "2", "3", "4"})
    void testIncreaseViewsCount_ShouldIncreaseViewCountOfNews_WhenCorrectNewsIdIsPassed(Long id) {
        when(this.newsRepository.findById(id))
                .thenReturn(Optional.of(news));

        this.newsService.increaseViewsCount(id);

        verify(this.newsRepository, times(1)).save(news);
    }

    @ParameterizedTest
    @CsvSource(value = {"-1", "-2", "-3", "-4"})
    void testIncreaseViewsCount_ShouldThrowException_WhenIncorrectNewsIdIsPassed(Long id) {
        when(this.newsRepository.findById(id))
                .thenThrow(RuntimeException.class);

        Executable executable = () -> this.newsService.increaseViewsCount(id);

        assertThrows(RuntimeException.class, executable);
    }

    @Test
    void testResetViews_ShouldResetViewsOfAllNews() {
        when(this.newsRepository.findAll())
                .thenReturn(newsEntityList);

        this.newsService.resetViews();

        newsEntityList.forEach(n -> verify(this.newsRepository, times(1)).save(n));
    }

    private static NewsEntity initNews() {
        return new NewsEntity()
                .setTitle("Title")
                .setContent("Content")
                .setCreatedOn(LocalDate.now())
                .setViews(1)
                .setImageUrl("image:/url")
                .setAuthor(initUser());
    }

    private static UserEntity initUser() {
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

    private static AddNewsDTO initAddNewsDto() {
        AddNewsDTO addNewsDTO = new AddNewsDTO();
        addNewsDTO.setContent("Content");
        addNewsDTO.setTitle("Title");
        addNewsDTO.setImageUrl("image:/url");
        return addNewsDTO;
    }

    private static NewsDTO initNewsDto() {
        return new NewsDTO(
                1L, "Title", "Content", "image:/url", 1, LocalDate.now()
        );
    }
}
