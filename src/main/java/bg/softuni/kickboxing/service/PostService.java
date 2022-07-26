package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.dto.post.AddPostDTO;
import bg.softuni.kickboxing.model.dto.post.PostDTO;
import bg.softuni.kickboxing.model.dto.post.PostDetailsDTO;
import bg.softuni.kickboxing.model.entity.PostEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.exception.ObjectNotFoundException;
import bg.softuni.kickboxing.model.user.GlovesOnPointUserDetails;
import bg.softuni.kickboxing.repository.PostRepository;
import bg.softuni.kickboxing.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public PostService(PostRepository postRepository, UserRepository userRepository, ModelMapper mapper, UserRoleService userRoleService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public void addPost(AddPostDTO addPostDTO, GlovesOnPointUserDetails userDetails) {
        PostEntity post = this.mapper.map(addPostDTO, PostEntity.class);

        post.setCreatedOn(LocalDateTime.now());

        UserEntity author = this.userRepository
                .findByUsername(userDetails.getUsername())
                .orElseThrow();
        post.setAuthor(author);

        if (userDetails.getRole().equals("ADMIN") || userDetails.getRole().equals("MODERATOR")) {
            post.setApproved(true);
        }

        this.postRepository.save(post);
    }

    public Page<PostDTO> getAllApprovedPostsOrderedByDateDesc(Pageable pageable) {
        return this.postRepository.getAllApprovedPostsOrderedByCreatedOnDesc(pageable);
    }

    public Page<PostDTO> getAllNotApprovedPostsOrderedByDateDesc(Pageable pageable) {
        return this.postRepository.getAllNotApprovedPostsOrderedByCreatedOnDesc(pageable);
    }

    public PostEntity findById(Long id) {
        return this.postRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id));
    }

    public PostDetailsDTO getPostDetailsById(Long id) {
        return this.postRepository
                .findById(id)
                .map(p -> this.mapper.map(p, PostDetailsDTO.class))
                .orElseThrow(() -> new ObjectNotFoundException(id));
    }

    public void increaseViewsCount(Long id) {
        PostEntity post = this.postRepository
                .findById(id)
                .orElseThrow();
        post.setViews(post.getViews() + 1);
        this.postRepository.save(post);
    }

    public void approvePost(Long id) {
        PostEntity post = this.postRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id));
        post.setApproved(true);
        this.postRepository.save(post);
    }

    public void disapprovePost(Long id) {
        if (id > this.getIdOfLastObjectInTable()) {
            throw new ObjectNotFoundException(id);
        }
        this.postRepository.deleteById(id);
    }

    public Long getIdOfLastObjectInTable() {
        return this.postRepository.findTopByOrderByIdDesc().getId();
    }
}
