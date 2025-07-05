package restful.api.SocialMediaApi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import restful.api.SocialMediaApi.dto.post.PostDTO;
import restful.api.SocialMediaApi.dto.post.PostResponseDTO;
import restful.api.SocialMediaApi.services.post.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Tag(name = "Посты", description = "Управление постами")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешный запрос"),
        @ApiResponse(responseCode = "400", description = "Неверный запрос"),
        @ApiResponse(responseCode = "404", description = "Ресурс не найден"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
})
@Validated
@SecurityRequirement(name = "JWT")
public class PostController {
    private final PostService postService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить пост по ID", description = "Выводит указанный пост и пользователя, создавшего этот пост")
    public ResponseEntity<PostResponseDTO> show(@PathVariable Long id) {
        return postService.findById(id);
    }

    @GetMapping("")
    @Operation(summary = "Получить все посты", description = "Выводит все посты из БД, указывая их ID и авторов")
    public ResponseEntity<List<PostResponseDTO>> index() {
        return ResponseEntity.ok(postService.findAll());
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Сохраняет пост с изображением", description = "Сохраняет пост в БД, возвращает информацию о посте, изображении и пользователе, создавшем этот пост")
    public ResponseEntity<PostResponseDTO> save(
            @RequestPart(name = "file") MultipartFile file,
            @RequestParam @NotBlank(message = "Post header cannot be empty") @Size(min = 5, max = 100, message = "Post header must be more than 5 characters and less than 100") String header,
            @RequestParam @NotBlank(message = "Post body cannot be empty") String body) {
        PostDTO postDTO = new PostDTO(header, body);
        return ResponseEntity.ok(postService.save(file, postDTO));
    }

    @PostMapping( "/add")
    @Operation(summary = "Сохраняет пост без изображения", description = "Сохраняет пост в БД, возвращает информацию о посте, изображении и пользователе, создавшем этот пост")
    public ResponseEntity<PostResponseDTO> save(@RequestBody @Valid PostDTO postDTO, BindingResult bindingResult) {
        return ResponseEntity.ok(postService.save(postDTO, bindingResult));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновляет пост без изображения", description = "Обновляет пост по ID в пути, возвращает информацию о посте, изображении и пользователе, создавшем этот пост. Не меняет пользователя")
    public ResponseEntity<PostResponseDTO> update(@PathVariable Long id, @RequestBody @Valid PostDTO postDTO, BindingResult bindingResult) {
        return ResponseEntity.ok(postService.update(id, postDTO, bindingResult));
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновляет пост с изображением", description = "Обновляет пост по ID в пути, возвращает информацию о посте, изображении и пользователе, создавшем этот пост. Не меняет пользователя")
    public ResponseEntity<PostResponseDTO> update(
            @PathVariable Long id,
            @RequestPart(name = "file") MultipartFile file,
            @RequestParam @NotBlank(message = "Post header cannot be empty") @Size(min = 5, max = 100, message = "Post header must be more than 5 characters and less than 100") String header,
            @RequestParam @NotBlank(message = "Post body cannot be empty") String body) {
        PostDTO postDTO = new PostDTO(header, body);
        return ResponseEntity.ok(postService.update(id, file, postDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаляет пост", description = "Удаляет пост, выводит информацию о посте и пользователе, создавшем этот пост")
    public ResponseEntity<PostResponseDTO> delete(@PathVariable Long id) {
        return ResponseEntity.ok(postService.delete(id));
    }

    @GetMapping("/feed")
    @Operation(summary = "Выводит ленту активности", description = "Выводит ленту постов от пользователей, на которых подписан аутентифицированный пользователь")
    public ResponseEntity<List<PostResponseDTO>> showActivityFeed(@RequestParam (required = false) @Min(0) Integer page, @RequestParam (required = false) @Min(1) Integer size) {
        return ResponseEntity.ok(postService.findAllByActivityFeed(page, size));
    }
}
