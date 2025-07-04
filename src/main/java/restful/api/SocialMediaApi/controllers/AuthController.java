package restful.api.SocialMediaApi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import restful.api.SocialMediaApi.dto.auth.JWTAuthResponse;
import restful.api.SocialMediaApi.dto.auth.LoginRequest;
import restful.api.SocialMediaApi.dto.auth.RegistrationRequest;
import restful.api.SocialMediaApi.services.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Аутентификация", description = "Регистрация и аутентификация пользователей")
public class AuthController {
    private final UserService userService;

    @PostMapping("/registration")
    @Operation(summary = "Регистрация нового пользователя", description = "Сохранение пользователя в БД, выдача ID пользователя и токена")
    public ResponseEntity<JWTAuthResponse> performRegistration(@Valid @RequestBody RegistrationRequest registrationRequest,
                                                               BindingResult bindingResult) {
        return ResponseEntity.ok(userService.save(registrationRequest, bindingResult));
    }

    @PostMapping("/login")
    @Operation(summary = "Аутентификация пользователя", description = "Аутентификация пользователя и выдача нового токена")
    public ResponseEntity<JWTAuthResponse> performLogin(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }
}
