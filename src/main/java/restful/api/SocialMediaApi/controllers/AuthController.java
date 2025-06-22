package restful.api.SocialMediaApi.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import restful.api.SocialMediaApi.dto.UserDTO;
import restful.api.SocialMediaApi.models.User;
import restful.api.SocialMediaApi.security.JwtUtil;
import restful.api.SocialMediaApi.services.UserService;
import restful.api.SocialMediaApi.util.UserMapper;
import restful.api.SocialMediaApi.util.UserValidator;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserMapper userMapper, UserValidator userValidator, UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userMapper = userMapper;
        this.userValidator = userValidator;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    //TODO Realize exception
    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid UserDTO userDTO,
                                                   BindingResult bindingResult) {
        User user = userMapper.toEntity(userDTO);
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return Map.of("Message", "Ошибка");
        }

        userService.save(user);

        String token = jwtUtil.generateToken(user.getUsername());
        return Map.of("token", token);
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody UserDTO userDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(),
                        userDTO.getPassword()
                );
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException ex){
            return Map.of("Message", "Invalid username or password");
        }

        String token = jwtUtil.generateToken(userDTO.getUsername());
        return Map.of("token", token);
    }
}
