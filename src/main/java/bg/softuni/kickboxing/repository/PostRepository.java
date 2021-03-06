package bg.softuni.kickboxing.repository;

import bg.softuni.kickboxing.model.dto.post.PostDTO;
import bg.softuni.kickboxing.model.entity.PostEntity;
import bg.softuni.kickboxing.model.enums.PostCategoryEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query("SELECT new bg.softuni.kickboxing.model.dto.post.PostDTO" +
            " (p.author.id, p.author.username, p.author.imageUrl, p.id, p.title, p.category, p.views, p.createdOn)" +
            " FROM PostEntity p" +
            " WHERE p.isApproved = TRUE" +
            " ORDER BY p.createdOn DESC")
    Page<PostDTO> getAllApprovedPostsOrderedByCreatedOnDesc(Pageable pageable);

    @Query("SELECT new bg.softuni.kickboxing.model.dto.post.PostDTO" +
            " (p.author.id, p.author.username, p.author.imageUrl, p.id, p.title, p.category, p.views, p.createdOn)" +
            " FROM PostEntity p" +
            " WHERE p.isApproved = TRUE and p.category = :category" +
            " ORDER BY p.createdOn DESC")
    Page<PostDTO> getAllApprovedPostsByCategoryOrderedByCreatedOnDesc(Pageable pageable, PostCategoryEnum category);

    @Query("SELECT new bg.softuni.kickboxing.model.dto.post.PostDTO" +
            " (p.author.id, p.author.username, p.author.imageUrl, p.id, p.title, p.category, p.views, p.createdOn)" +
            " FROM PostEntity p" +
            " WHERE p.isApproved = TRUE and p.title LIKE %:query%" +
            " ORDER BY p.createdOn DESC")
    Page<PostDTO> getAllApprovedPostsWhereTitleLike(Pageable pageable, String query);

    @Query("SELECT new bg.softuni.kickboxing.model.dto.post.PostDTO" +
            " (p.author.id, p.author.username, p.author.imageUrl, p.id, p.title, p.category, p.views, p.createdOn)" +
            " FROM PostEntity p" +
            " WHERE p.isApproved = FALSE" +
            " ORDER BY p.createdOn DESC")
    Page<PostDTO> getAllNotApprovedPostsOrderedByCreatedOnDesc(Pageable pageable);

    PostEntity findTopByOrderByIdDesc();
}
