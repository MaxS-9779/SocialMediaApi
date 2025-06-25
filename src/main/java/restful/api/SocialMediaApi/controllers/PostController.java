package restful.api.SocialMediaApi.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import restful.api.SocialMediaApi.dto.post.PostDTO;
import restful.api.SocialMediaApi.models.Post;
import restful.api.SocialMediaApi.services.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDTO> show(@PathVariable Long id) {
        return postService.findById(id);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> index() {
        return ResponseEntity.ok(postService.findAll());
    }

    @PostMapping("/posts/add")
    public ResponseEntity<Post> save(@RequestBody @Valid PostDTO postDTO, BindingResult bindingResult) {
        return ResponseEntity.ok(postService.save(postDTO, bindingResult));
    }

    @PatchMapping("/posts/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @RequestBody @Valid PostDTO postDTO, BindingResult bindingResult) {
        return ResponseEntity.ok(postService.update(id, postDTO, bindingResult));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Post> delete(@PathVariable Long id) {
        return ResponseEntity.ok(postService.delete(id));
    }
}
