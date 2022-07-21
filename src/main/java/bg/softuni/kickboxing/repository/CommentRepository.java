package bg.softuni.kickboxing.repository;

import bg.softuni.kickboxing.model.dto.comment.CommentDTO;
import bg.softuni.kickboxing.model.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("SELECT new bg.softuni.kickboxing.model.dto.comment.CommentDTO" +
            " (c.id, c.content, c.createdOn, c.author.username, c.author.imageUrl)" +
            " FROM CommentEntity c" +
            " WHERE c.isApproved = FALSE" +
            " ORDER BY c.createdOn DESC")
    Page<CommentDTO> getAllNotApprovedCommentsOrderedByCreatedOnDesc(Pageable pageable);

    CommentEntity findTopByOrderByIdDesc();
}
