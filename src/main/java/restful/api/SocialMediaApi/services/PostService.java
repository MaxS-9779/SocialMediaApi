package restful.api.SocialMediaApi.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import restful.api.SocialMediaApi.dto.post.PostDTO;
import restful.api.SocialMediaApi.exceptions.PostNotFoundException;
import restful.api.SocialMediaApi.mappers.PostMapper;
import restful.api.SocialMediaApi.models.Post;
import restful.api.SocialMediaApi.repositories.PostRepository;
import restful.api.SocialMediaApi.validators.PostValidator;

import java.util.List;

@Service
@AllArgsConstructor
public class PostService {
    private final PostMapper postMapper;
    private final PostValidator postValidator;
    private PostRepository postRepository;

    public ResponseEntity<PostDTO> findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post with this id not found"));
        PostDTO postDTO = postMapper.toDTO(post);

        return ResponseEntity.ok(postDTO);
    }

    public List<PostDTO> findAll() {
        List<Post> posts = postRepository.findAll();

        return posts.stream().map(postMapper::toDTO).toList();
    }

    @Transactional
    public Post save(PostDTO postDTO, BindingResult bindingResult) {
        Post post = postMapper.toPost(postDTO);
        postValidator.validate(post, bindingResult);

        //выбросится ошибка и не сохранит в бд
        PostValidator.bindingResultValidation(bindingResult);

            return postRepository.save(post);

    }

    @Transactional
    public Post update(Long id, PostDTO postDTO, BindingResult bindingResult) {
        PostValidator.bindingResultValidation(bindingResult);

        //выбросится ошибка и не обновит в бд
        Post post = postMapper.toPost(postDTO);
        post.setId(id);

        return postRepository.save(post);
    }

    @Transactional
    public Post delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post with this id not found"));
        postRepository.deleteById(id);
        return post;
    }
}
