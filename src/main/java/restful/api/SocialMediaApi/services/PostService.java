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
import org.springframework.web.multipart.MultipartFile;
import restful.api.SocialMediaApi.dto.image.ImageDTO;
import restful.api.SocialMediaApi.dto.post.PostDTO;
import restful.api.SocialMediaApi.dto.post.PostResponseDTO;
import restful.api.SocialMediaApi.exceptions.EntityNotFoundException;
import restful.api.SocialMediaApi.exceptions.PostUpdateException;
import restful.api.SocialMediaApi.exceptions.ValidateException;
import restful.api.SocialMediaApi.mappers.ImageMapper;
import restful.api.SocialMediaApi.mappers.PostMapper;
import restful.api.SocialMediaApi.models.Image;
import restful.api.SocialMediaApi.models.Post;
import restful.api.SocialMediaApi.models.User;
import restful.api.SocialMediaApi.repositories.PostRepository;
import restful.api.SocialMediaApi.repositories.SubscribeRepository;
import restful.api.SocialMediaApi.util.AuthenticatedUser;
import restful.api.SocialMediaApi.validators.PostValidator;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostMapper postMapper;
    private final PostValidator postValidator;
    private final SubscribeRepository subscribeRepository;
    private final PostRepository postRepository;
    private final FileStorageService fileStorageService;
    private final ImageMapper imageMapper;


    public ResponseEntity<PostResponseDTO> findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post with this id not found"));
        PostResponseDTO postResponseDTO = postMapper.toPostResponseDTO(post);

        if (post.getImage() != null) {
            ImageDTO imageDTO = imageMapper.toImageDTO(post.getImage());
            postResponseDTO.setImage(imageDTO);
        }
        return ResponseEntity.ok(postResponseDTO);
    }


    public List<PostResponseDTO> findAll() {
        return postRepository.findAll()
                .stream()
                .map(post -> {
                    PostResponseDTO postResponseDTO = postMapper.toPostResponseDTO(post);
                    if (post.getImage() != null) {
                        postResponseDTO.setImage(imageMapper.toImageDTO(post.getImage()));
                    }
                    return postResponseDTO;
                }).toList();
    }


    @Transactional
    public PostResponseDTO save(MultipartFile file, PostDTO postDTO) {
        Image image = null;
        if (file != null) {
            try {
                fileStorageService.saveFile(file);
                image = fileStorageService.getImage(file);
            } catch (IOException e) {
                throw new ValidateException("File save failed");
            }
        }

        Post post = postMapper.toPost(postDTO);
        post.setUser(AuthenticatedUser.getUserFromContext());
        post.setImage(image);

        Errors errors = new BeanPropertyBindingResult(post, "post");
        postValidator.validate(post, errors);

        //выбросится ошибка и не сохранит в бд
        PostValidator.bindingResultValidation(errors);

        postRepository.save(post);

        PostResponseDTO postResponseDTO = postMapper.toPostResponseDTO(post);
        postResponseDTO.setImage(imageMapper.toImageDTO(image));
        return postResponseDTO;
    }

    @Transactional
    public PostResponseDTO save(PostDTO postDTO, BindingResult bindingResult) {

        Post post = postMapper.toPost(postDTO);
        post.setUser(AuthenticatedUser.getUserFromContext());

        postValidator.validate(post, bindingResult);

        //выбросится ошибка и не сохранит в бд
        PostValidator.bindingResultValidation(bindingResult);

        postRepository.save(post);

        return postMapper.toPostResponseDTO(post);
    }

    @Transactional
    public PostResponseDTO update(Long id, PostDTO postDTO, BindingResult bindingResult) {
        //достаем пост из БД и проверяем соответствие пользователя
        Post updatedPost = findPostAndCheckUserCompliance(id);

        Post newPost = postMapper.toPost(postDTO);

        postValidator.validate(newPost, bindingResult);
        PostValidator.bindingResultValidation(bindingResult);

        newPost.setId(id);
        newPost.setUser(updatedPost.getUser());

        postRepository.save(newPost);

        return postMapper.toPostResponseDTO(newPost);
    }


    @Transactional
    public PostResponseDTO update(Long id, MultipartFile file, PostDTO postDTO) {
        //достаем пост из БД и проверяем соответствие пользователя
        Post updatedPost = findPostAndCheckUserCompliance(id);

        Image image = null;
        if (file != null) {
            try {
                fileStorageService.saveFile(file);
                image = fileStorageService.getImage(file);
            } catch (IOException e) {
                throw new ValidateException("File save failed");
            }
        }

        Post newPost = postMapper.toPost(postDTO);
        newPost.setImage(image);

        Errors errors = new BeanPropertyBindingResult(postDTO, "post");
        postValidator.validate(newPost, errors);
        PostValidator.bindingResultValidation(errors);


        newPost.setId(id);
        newPost.setUser(updatedPost.getUser());

        postRepository.save(newPost);

        PostResponseDTO postResponseDTO = postMapper.toPostResponseDTO(newPost);
        postResponseDTO.setImage(imageMapper.toImageDTO(image));
        return postResponseDTO;
    }

    @Transactional
    public PostResponseDTO delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post with this id not found"));
        checkUserCompliance(post, "You are not allowed to delete this post");

        if (post.getImage() != null) {
            try {
                FileStorageService.deleteFile(post.getImage());
            } catch (IOException e) {
                throw new RuntimeException("File delete failed");
            }
        }
        postRepository.deleteById(id);

        return postMapper.toPostResponseDTO(post);
    }

    public List<PostResponseDTO> findAllByActivityFeed(Integer pageNumber, Integer pageSize) {
        List<Long> subscribedUserIds = subscribeRepository.findByFromUser(AuthenticatedUser.getUserFromContext())
                .stream().map(subscribe -> subscribe.getToUser().getId())
                .toList();
        List<Post> posts;
        Sort sort = Sort.by("createdAt").descending();

        if (pageNumber == null || pageSize == null) {
            posts = postRepository.findByUserIdIn(subscribedUserIds, sort);
        } else {
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
            posts = postRepository.findByUserIdIn(subscribedUserIds, pageable);
        }

        return posts.stream().map(postMapper::toPostResponseDTO).toList();
    }


    private Post findPostAndCheckUserCompliance(Long id) {
        Post updatedPost = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post with this id not found"));
        //проверяем свой ли пост редактирует авторизованный пользователь, если не свой -> ошибка
        checkUserCompliance(updatedPost, "You are not allowed to update this post");
        return updatedPost;
    }

    private static void checkUserCompliance(Post updatedPost, String message) {
        User user = AuthenticatedUser.getUserFromContext();
        if (!updatedPost.getUser().getEmail().equals(user.getEmail())) {
            throw new PostUpdateException(message);
        }
    }
}
