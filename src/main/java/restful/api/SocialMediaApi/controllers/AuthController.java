package restful.api.SocialMediaApi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restful.api.SocialMediaApi.dto.auth.JWTAuthResponse;
import restful.api.SocialMediaApi.dto.auth.LoginRequest;
import restful.api.SocialMediaApi.dto.auth.RegistrationRequest;
import restful.api.SocialMediaApi.services.auth.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Аутентификация", description = "Регистрация и аутентификация пользователей")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/registration")
    @Operation(summary = "Регистрация нового пользователя", description = "Сохранение пользователя в БД, выдача JWTAuthResponse (id, username, email, token)")
    public ResponseEntity<JWTAuthResponse> performRegistration(@Valid @RequestBody RegistrationRequest registrationRequest,
                                                               BindingResult bindingResult) {
        return ResponseEntity.ok(authService.save(registrationRequest, bindingResult));
    }

    @PostMapping("/login")
    @Operation(summary = "Аутентификация пользователя", description = "Аутентификация пользователя, выдача JWTAuthResponse (id, username, email, token(новый))")
    public ResponseEntity<JWTAuthResponse> performLogin(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}
