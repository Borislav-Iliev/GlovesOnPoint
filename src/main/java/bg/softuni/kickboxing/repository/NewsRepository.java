package bg.softuni.kickboxing.repository;

import bg.softuni.kickboxing.model.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
    List<NewsEntity> findAllByOrderByDateDescIdDesc();
}
