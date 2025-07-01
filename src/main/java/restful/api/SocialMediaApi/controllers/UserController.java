package restful.api.SocialMediaApi.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restful.api.SocialMediaApi.dto.user.UserDTO;
import restful.api.SocialMediaApi.dto.user.UserGetDTO;
import restful.api.SocialMediaApi.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Взаимодействие с пользователями")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<List<UserDTO>> index() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<UserGetDTO> show(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
}
