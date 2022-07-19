package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.dto.post.AddPostDTO;
import bg.softuni.kickboxing.model.dto.post.PostDTO;
import bg.softuni.kickboxing.model.entity.PostEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.user.KickboxingUserDetails;
import bg.softuni.kickboxing.repository.PostRepository;
import bg.softuni.kickboxing.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final UserRoleService userRoleService;

    public PostService(PostRepository postRepository, UserRepository userRepository, ModelMapper mapper, UserRoleService userRoleService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.userRoleService = userRoleService;
    }

    public void addPost(AddPostDTO addPostDTO, KickboxingUserDetails userDetails) {
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

    public List<PostDTO> getAllNotApprovedPostsOrderedByDateDesc() {
        return this.postRepository.getAllNotApprovedPostsOrderedByCreatedOnDesc();
    }

    public PostDTO getPostById(Long id) {
        return this.postRepository
                .findById(id)
                .map(p -> this.mapper.map(p, PostDTO.class))
                .orElse(null);
    }

    public void increaseViewsCountById(Long id) {
        PostEntity post = this.postRepository
                .findById(id)
                .orElseThrow();
        post.setViews(post.getViews() + 1);
        this.postRepository.save(post);
    }

    public void approvePost(Long id) {
        PostEntity post = this.postRepository.findById(id).orElseThrow();
        post.setApproved(true);
        this.postRepository.save(post);
    }

    public void disapprovePost(Long id) {
        this.postRepository.deleteById(id);
    }
}
