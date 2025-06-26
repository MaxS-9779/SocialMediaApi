package restful.api.SocialMediaApi.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import restful.api.SocialMediaApi.dto.post.PostDTO;
import restful.api.SocialMediaApi.dto.post.PostPatchDTO;
import restful.api.SocialMediaApi.dto.post.PostResponseDTO;
import restful.api.SocialMediaApi.models.Post;
import restful.api.SocialMediaApi.services.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDTO> show(@PathVariable Long id) {
        return postService.findById(id);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDTO>> index() {
        return ResponseEntity.ok(postService.findAll());
    }

    @PostMapping("/posts/add")
    public ResponseEntity<PostResponseDTO> save(@RequestBody @Valid PostDTO postDTO, BindingResult bindingResult) {
        return ResponseEntity.ok(postService.save(postDTO, bindingResult));
    }

    @PatchMapping("/posts/{id}")
    public ResponseEntity<PostResponseDTO> update(@PathVariable Long id, @RequestBody @Valid PostPatchDTO postPatchDTO, BindingResult bindingResult) {
        return ResponseEntity.ok(postService.update(id, postPatchDTO, bindingResult));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<PostResponseDTO> delete(@PathVariable Long id) {
        return ResponseEntity.ok(postService.delete(id));
    }
}
