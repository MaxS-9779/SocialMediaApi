package restful.api.SocialMediaApi.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import restful.api.SocialMediaApi.dto.auth.LoginRequest;
import restful.api.SocialMediaApi.dto.auth.RegistrationRequest;
import restful.api.SocialMediaApi.dto.auth.JWTAuthResponse;
import restful.api.SocialMediaApi.dto.user.UserDTO;
import restful.api.SocialMediaApi.dto.user.UserResponseDTO;
import restful.api.SocialMediaApi.exceptions.AuthenticationException;
import restful.api.SocialMediaApi.exceptions.EntityNotFoundException;
import restful.api.SocialMediaApi.mappers.UserMapper;
import restful.api.SocialMediaApi.exceptions.RegistrationException;
import restful.api.SocialMediaApi.models.User;
import restful.api.SocialMediaApi.repositories.SubscribeRepository;
import restful.api.SocialMediaApi.repositories.UserRepository;
import restful.api.SocialMediaApi.util.AuthenticatedUser;
import restful.api.SocialMediaApi.security.JwtUtil;
import restful.api.SocialMediaApi.validators.UserValidator;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final static Logger LOGGER = LogManager.getLogger(UserService.class);
    private final SubscribeRepository subscribeRepository;

    public List<UserDTO> findAll() {
        //Достаем List с user'ами из репозитория
        List<User> users = userRepository.findAll();
        //Маппим лист юзеров в лист UserResponseDTO и возвращаем
        return users.stream().map(userMapper::toUserDTO).toList();
    }

    public UserResponseDTO findById(Long id) {
        User toUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with this id not found"));
        User fromUser = AuthenticatedUser.getUserFromContext();
        if (toUser.equals(fromUser)) {
            return new UserResponseDTO(userMapper.toUserDTO(toUser));
        }
        UserResponseDTO userResponseDTO = new UserResponseDTO(userMapper.toUserDTO(toUser), "To message this user you must be friends");

        //проверяем наличие подписки
        subscribeRepository.findByToUserAndFromUser(toUser, fromUser).ifPresent(subscribe -> {
            //если подписка двусторонняя (дружба) -> выводим сообщение о том, что пользователь может писать сообщение
            if (subscribe.isMutual()) {
                userResponseDTO.setMessage("You can write message to this User");
            }
        });

        return userResponseDTO;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public JWTAuthResponse save(RegistrationRequest registrationRequest, BindingResult bindingResult) {

        User user = userMapper.toUser(registrationRequest);
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMsg.append(fieldError.getField())
                        .append(" - ").append(fieldError.getDefaultMessage())
                        .append(";");
            }
            throw new RegistrationException(errorMsg.toString());
        } else {

            user.setCreationDate(LocalDateTime.now());
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(user);

            JWTAuthResponse JWTAuthResponse = userMapper.toJWTAuthResponse(user);
            JWTAuthResponse.setToken(jwtUtil.generateToken(user.getUsername(), user.getEmail()));

            return JWTAuthResponse;
        }
    }

    public JWTAuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new EntityNotFoundException("User with this email not found"));

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), loginRequest.getPassword());
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException ex) {
            throw new AuthenticationException("Invalid username or password");
        }

        JWTAuthResponse JWTAuthResponse = userMapper.toJWTAuthResponse(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getEmail());

        JWTAuthResponse.setToken(token);
        JWTAuthResponse.setId(user.getId());

        return JWTAuthResponse;
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
