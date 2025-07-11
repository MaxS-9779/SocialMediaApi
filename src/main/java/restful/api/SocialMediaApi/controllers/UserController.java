package restful.api.SocialMediaApi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restful.api.SocialMediaApi.dto.user.UserDTO;
import restful.api.SocialMediaApi.dto.user.UserResponseDTO;
import restful.api.SocialMediaApi.services.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Взаимодействие с пользователями")
@SecurityRequirement(name = "JWT")
public class UserController {
    private final UserService userService;

    @GetMapping("")
    @Operation(summary = "Возвращает всех пользователей", description = "Выводит из БД всех пользователей")

    public ResponseEntity<List<UserDTO>> index() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Возвращает пользователя по ID", description = "Выводит пользователя, а также сообщение о возможности переписки с этим пользователем")
    public ResponseEntity<UserResponseDTO> show(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
}
