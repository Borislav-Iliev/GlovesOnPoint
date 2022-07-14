package bg.softuni.kickboxing.service;

import bg.softuni.kickboxing.model.dto.post.AddPostDTO;
import bg.softuni.kickboxing.model.dto.post.PostInformationDTO;
import bg.softuni.kickboxing.model.entity.PostEntity;
import bg.softuni.kickboxing.model.entity.UserEntity;
import bg.softuni.kickboxing.model.user.KickboxingUserDetails;
import bg.softuni.kickboxing.repository.PostRepository;
import bg.softuni.kickboxing.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public PostService(PostRepository postRepository, UserRepository userRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public void addPost(AddPostDTO addPostDTO, KickboxingUserDetails userDetails) {
        PostEntity post = this.mapper.map(addPostDTO, PostEntity.class);

        post.setCreatedOn(LocalDateTime.now());

        UserEntity author = this.userRepository
                .findByUsername(userDetails.getUsername())
                .orElseThrow();
        post.setAuthor(author);

        this.postRepository.save(post);
    }

    public List<PostInformationDTO> getAllPostsOrderedByDateDesc() {
        return this.postRepository.getAllPostOrderedByCreatedOnDesc();
    }

    public PostInformationDTO getPostById(Long id) {
        return this.postRepository
                .findById(id)
                .map(p -> this.mapper.map(p, PostInformationDTO.class))
                .orElse(null);
    }

    public void increaseViewsCountById(Long id) {
        PostEntity post = this.postRepository
                .findById(id)
                .orElseThrow();
        post.setViews(post.getViews() + 1);
        this.postRepository.save(post);
    }
}
