package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.dto.comment.AddCommentDTO;
import bg.softuni.kickboxing.model.dto.comment.CommentDTO;
import bg.softuni.kickboxing.model.entity.CommentEntity;
import bg.softuni.kickboxing.model.entity.PostEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.exception.ObjectNotFoundException;
import bg.softuni.kickboxing.model.user.GlovesOnPointUserDetails;
import bg.softuni.kickboxing.repository.CommentRepository;
import bg.softuni.kickboxing.repository.PostRepository;
import bg.softuni.kickboxing.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper mapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, ModelMapper mapper, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.mapper = mapper;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void addComment(Long id, AddCommentDTO addCommentDTO, GlovesOnPointUserDetails userDetails) {
        CommentEntity comment = this.mapper.map(addCommentDTO, CommentEntity.class);

        comment.setCreatedOn(LocalDateTime.now());

        PostEntity post = this.postRepository
                .findById(id)
                .orElseThrow();
        comment.setPost(post);

        UserEntity author = this.userRepository
                .findByUsername(userDetails.getUsername())
                .orElseThrow();
        comment.setAuthor(author);

        if (userDetails.getRole().equals("ADMIN") || userDetails.getRole().equals("MODERATOR")) {
            comment.setApproved(true);
        }

        this.commentRepository.save(comment);
    }

    public Page<CommentDTO> getAllNotApprovedCommentsOrderedByDateDesc(Pageable pageable) {
        return this.commentRepository.getAllNotApprovedCommentsOrderedByCreatedOnDesc(pageable);
    }

    public void approveComment(Long id) {
        CommentEntity comment = this.commentRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id));
        comment.setApproved(true);
        this.commentRepository.save(comment);
    }

    public void disapproveComment(Long id) {
        if (id > this.getIdOfLastObjectInTable()) {
            throw new ObjectNotFoundException(id);
        }
        this.commentRepository.deleteById(id);
    }

    public Long getIdOfLastObjectInTable() {
        return this.commentRepository.findTopByOrderByIdDesc().getId();
    }
}
