package restful.api.SocialMediaApi.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import restful.api.SocialMediaApi.dto.user.UserDTO;
import restful.api.SocialMediaApi.dto.auth.RegistrationLoginDTO;
import restful.api.SocialMediaApi.mappers.UserMapper;
import restful.api.SocialMediaApi.security.JwtUtil;
import restful.api.SocialMediaApi.services.UserService;
import restful.api.SocialMediaApi.validators.UserValidator;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserMapper userMapper, UserValidator userValidator, UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<RegistrationLoginDTO> performRegistration(@Valid @RequestBody UserDTO userDTO,
                                                                    BindingResult bindingResult) {
        return ResponseEntity.ok(userService.save(userDTO, bindingResult));
    }

    @PostMapping("/login")
    public ResponseEntity<RegistrationLoginDTO> performLogin(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.login(userDTO));
    }
}
