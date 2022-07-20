package bg.softuni.kickboxing.repository;

import bg.softuni.kickboxing.model.dto.comment.CommentDTO;
import bg.softuni.kickboxing.model.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("SELECT new bg.softuni.kickboxing.model.dto.comment.CommentDTO" +
            " (c.content, c.createdOn)" +
            " FROM CommentEntity c" +
            " WHERE c.isApproved = FALSE" +
            " ORDER BY c.createdOn DESC")
    List<CommentDTO> getAllNotApprovedCommentsOrderedByCreatedOnDesc();
}
