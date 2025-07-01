package restful.api.SocialMediaApi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import restful.api.SocialMediaApi.dto.auth.JWTAuthResponse;
import restful.api.SocialMediaApi.dto.auth.RegistrationRequest;
import restful.api.SocialMediaApi.mappers.UserMapper;
import restful.api.SocialMediaApi.security.JwtUtil;
import restful.api.SocialMediaApi.services.UserService;
import restful.api.SocialMediaApi.validators.UserValidator;

@RestController
@RequestMapping("/auth")
@Tag(name = "Аутентификация", description = "Регистрация и аутентификация пользователей")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserMapper userMapper, UserValidator userValidator, UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    @Operation(summary = "Регистрация нового пользователя", description = "Сохранение пользователя в БД, выдача ID пользователя и токена")
    public ResponseEntity<JWTAuthResponse> performRegistration(@Valid @RequestBody RegistrationRequest registrationRequest,
                                                               BindingResult bindingResult) {
        return ResponseEntity.ok(userService.save(registrationRequest, bindingResult));
    }

    @PostMapping("/login")
    @Operation(summary = "Аутентификация пользователя", description = "Аутентификация пользователя и выдача нового токена")
    public ResponseEntity<JWTAuthResponse> performLogin(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(userService.login(registrationRequest));
    }
}
