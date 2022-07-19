package bg.softuni.kickboxing.repository;

import bg.softuni.kickboxing.model.dto.news.NewsDTO;
import bg.softuni.kickboxing.model.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long> {

    @Query("SELECT new bg.softuni.kickboxing.model.dto.news.NewsDTO" +
            " (n.id, n.title, n.content, n.imageUrl, n.date)" +
            " FROM NewsEntity n" +
            " ORDER BY n.date DESC, n.id DESC")
    List<NewsDTO> findAllByOrderByDateDescIdDesc();

    NewsEntity findTopByOrderByIdDesc();
}
