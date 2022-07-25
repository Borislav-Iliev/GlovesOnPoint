package bg.softuni.kickboxing.repository;

import bg.softuni.kickboxing.model.dto.news.NewsDTO;
import bg.softuni.kickboxing.model.entity.NewsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long> {

    @Query("SELECT new bg.softuni.kickboxing.model.dto.news.NewsDTO" +
            " (n.id, n.title, n.content, n.imageUrl, n.views, n.createdOn)" +
            " FROM NewsEntity n" +
            " ORDER BY n.createdOn DESC, n.id DESC")
    Page<NewsDTO> findAllByOrderByCreatedOnDescIdDesc(Pageable pageable);

    @Query("SELECT new bg.softuni.kickboxing.model.dto.news.NewsDTO" +
            " (n.id, n.title, n.content, n.imageUrl, n.views, n.createdOn)" +
            " FROM NewsEntity n" +
            " ORDER BY n.views DESC, n.createdOn DESC, n.id DESC")
    List<NewsDTO> findTrendingNewsOrderByCreatedOnDescIdDesc();

    NewsEntity findTopByOrderByIdDesc();
}
