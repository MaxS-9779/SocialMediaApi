package restful.api.SocialMediaApi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import restful.api.SocialMediaApi.dto.post.PostDTO;
import restful.api.SocialMediaApi.dto.post.PostResponseDTO;
import restful.api.SocialMediaApi.services.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Посты", description = "Управление постами")
public class PostController {
    private final PostService postService;

    @GetMapping("/posts/{id}")
    @Operation(summary = "Получить пост по ID", description = "Выводит указанный пост и пользователя, создавшего этот пост")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<PostResponseDTO> show(@PathVariable Long id) {
        return postService.findById(id);
    }

    @GetMapping("/posts")
    @Operation(summary = "Получить все посты", description = "Выводит все посты из БД, указывая их ID и авторов")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<List<PostResponseDTO>> index() {
        return ResponseEntity.ok(postService.findAll());
    }

    @PostMapping("/posts/add")
    @Operation(summary = "Сохраняет пост", description = "Сохраняет пост в БД, возвращает информацию о посте и пользователе, создавшем этот пост")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<PostResponseDTO> save(@RequestBody @Valid PostDTO postDTO, BindingResult bindingResult) {
        return ResponseEntity.ok(postService.save(postDTO, bindingResult));
    }

    @PatchMapping("/posts/{id}")
    @Operation(summary = "Обновляет пост", description = "Обновляет пост по ID в пути, возвращает информацию о посте и пользователе, создавшем этот пост. Не меняет пользователя")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<PostResponseDTO> update(@PathVariable Long id, @RequestBody @Valid PostDTO postDTO, BindingResult bindingResult) {
        return ResponseEntity.ok(postService.update(id, postDTO, bindingResult));
    }

    @DeleteMapping("/posts/{id}")
    @Operation(summary = "Удаляет пост", description = "Удаляет пост, выводит информацию о посте и пользователе, создавшем этот пост")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<PostResponseDTO> delete(@PathVariable Long id) {
        return ResponseEntity.ok(postService.delete(id));
    }
}
