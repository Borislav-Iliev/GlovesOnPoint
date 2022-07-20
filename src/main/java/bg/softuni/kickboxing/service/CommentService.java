package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.dto.comment.AddCommentDTO;
import bg.softuni.kickboxing.model.dto.comment.CommentDTO;
import bg.softuni.kickboxing.model.entity.CommentEntity;
import bg.softuni.kickboxing.model.entity.PostEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.user.KickboxingUserDetails;
import bg.softuni.kickboxing.repository.CommentRepository;
import bg.softuni.kickboxing.repository.PostRepository;
import bg.softuni.kickboxing.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public void addComment(Long id, AddCommentDTO addCommentDTO, KickboxingUserDetails userDetails) {
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
}
