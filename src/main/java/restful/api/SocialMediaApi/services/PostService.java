package restful.api.SocialMediaApi.services;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import restful.api.SocialMediaApi.dto.post.PostDTO;
import restful.api.SocialMediaApi.dto.post.PostResponseDTO;
import restful.api.SocialMediaApi.exceptions.PostNotFoundException;
import restful.api.SocialMediaApi.exceptions.PostUpdateException;
import restful.api.SocialMediaApi.mappers.PostMapper;
import restful.api.SocialMediaApi.models.Post;
import restful.api.SocialMediaApi.models.User;
import restful.api.SocialMediaApi.repositories.PostRepository;
import restful.api.SocialMediaApi.security.UserDetails;
import restful.api.SocialMediaApi.validators.PostValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private final PostMapper postMapper;
    private final PostValidator postValidator;
    private PostRepository postRepository;

    public ResponseEntity<PostResponseDTO> findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post with this id not found"));

        return ResponseEntity.ok(postMapper.toPostResponseDTO(post));
    }

    public List<PostResponseDTO> findAll() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(postMapper::toPostResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    public PostResponseDTO save(PostDTO postDTO, BindingResult bindingResult) {
        Post post = postMapper.toPost(postDTO);
        post.setUser(getUserFromContext());

        postValidator.validate(postMapper.toPost(postDTO), bindingResult);

        //выбросится ошибка и не сохранит в бд
        PostValidator.bindingResultValidation(bindingResult);
        postRepository.save(post);

        return postMapper.toPostResponseDTO(post);

    }

    @Transactional
    public PostResponseDTO update(Long id, PostDTO postDTO, BindingResult bindingResult) {
        Post newPost = postMapper.toPost(postDTO);

        postValidator.validate(newPost, bindingResult);
        PostValidator.bindingResultValidation(bindingResult);


        Post updatedPost = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post with this id not found"));

        //проверяем свой ли пост редактирует авторизованный пользователь, если не свой -> ошибка
        User user = getUserFromContext();
        if (!updatedPost.getUser().getEmail().equals(user.getEmail())) {
            throw new PostUpdateException("You are not allowed to update this post");
        }


            newPost.setId(id);
            newPost.setUser(updatedPost.getUser());

            postRepository.save(newPost);

            return postMapper.toPostResponseDTO(newPost);

    }

    @Transactional
    public PostResponseDTO delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post with this id not found"));
        User user = getUserFromContext();
        if (!post.getUser().getEmail().equals(user.getEmail())) {
            throw new PostUpdateException("You are not allowed to delete this post");
        }
        postRepository.deleteById(id);
        return postMapper.toPostResponseDTO(post);
    }

    private User getUserFromContext() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }
}
