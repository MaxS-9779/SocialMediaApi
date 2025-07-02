package restful.api.SocialMediaApi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import restful.api.SocialMediaApi.dto.post.PostDTO;
import restful.api.SocialMediaApi.dto.post.PostResponseDTO;
import restful.api.SocialMediaApi.exceptions.EntityNotFoundException;
import restful.api.SocialMediaApi.exceptions.PostUpdateException;
import restful.api.SocialMediaApi.mappers.PostMapper;
import restful.api.SocialMediaApi.models.Post;
import restful.api.SocialMediaApi.models.User;
import restful.api.SocialMediaApi.repositories.PostRepository;
import restful.api.SocialMediaApi.repositories.SubscribeRepository;
import restful.api.SocialMediaApi.util.AuthenticatedUser;
import restful.api.SocialMediaApi.validators.PostValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostMapper postMapper;
    private final PostValidator postValidator;
    private final SubscribeRepository subscribeRepository;
    private final PostRepository postRepository;

    public ResponseEntity<PostResponseDTO> findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post with this id not found"));

        return ResponseEntity.ok(postMapper.toPostResponseDTO(post));
    }

    public List<PostResponseDTO> findAll() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(postMapper::toPostResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    public PostResponseDTO save(PostDTO postDTO, BindingResult bindingResult) {
        Post post = postMapper.toPost(postDTO);
        post.setUser(AuthenticatedUser.getUserFromContext());

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


        Post updatedPost = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post with this id not found"));

        //проверяем свой ли пост редактирует авторизованный пользователь, если не свой -> ошибка
        User user = AuthenticatedUser.getUserFromContext();
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
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post with this id not found"));
        User user = AuthenticatedUser.getUserFromContext();
        if (!post.getUser().getEmail().equals(user.getEmail())) {
            throw new PostUpdateException("You are not allowed to delete this post");
        }
        postRepository.deleteById(id);
        return postMapper.toPostResponseDTO(post);
    }

    public List<PostResponseDTO> findAllByActivityFeed(Integer pageNumber, Integer pageSize) {
        Errors errors = new BeanPropertyBindingResult(new Post(), "post");
        List<Long> subscribedUserIds = subscribeRepository.findByFromUser(AuthenticatedUser.getUserFromContext())
                .stream().map(subscribe -> subscribe.getToUser().getId())
                .toList();
        List<Post> posts;
        Sort sort = Sort.by("createdAt").descending();

        if (errors.hasErrors()) {
            posts = postRepository.findByUserIdIn(subscribedUserIds, sort);
            return posts.stream().map(postMapper::toPostResponseDTO).toList();
        }

         else {
            //Получаем всех пользователей, на которых подписан аутентифицированный юзер
            //Затем получаем ID этих самых пользователей

            //сортировка

            Pageable pageable;


            pageable = PageRequest.of(pageNumber, pageSize, sort);
            posts = postRepository.findByUserIdIn(subscribedUserIds, pageable);
        }

        return posts.stream().map(postMapper::toPostResponseDTO).toList();
    }
}
