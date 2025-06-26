package restful.api.SocialMediaApi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restful.api.SocialMediaApi.dto.auth.UserResponseDTO;
import restful.api.SocialMediaApi.services.UserService;

import java.util.List;

@RestController
@RequestMapping("")
@Tag(name = "Пользователи", description = "Взаимодействие с пользователями")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> index() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> show(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
}
