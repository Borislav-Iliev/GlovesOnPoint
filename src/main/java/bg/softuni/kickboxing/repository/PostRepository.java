package bg.softuni.kickboxing.repository;

import bg.softuni.kickboxing.model.dto.post.PostInformationDTO;
import bg.softuni.kickboxing.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query("SELECT new bg.softuni.kickboxing.model.dto.post.PostInformationDTO" +
            " (p.author.id, p.author.username, p.id, p.title, p.category, p.views, p.createdOn)" +
            " FROM PostEntity p" +
            " ORDER BY p.createdOn DESC")
    List<PostInformationDTO> getAllPostOrderedByCreatedOnDesc();
}
