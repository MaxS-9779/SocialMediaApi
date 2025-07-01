package restful.api.SocialMediaApi.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import restful.api.SocialMediaApi.dto.auth.RegistrationRequest;
import restful.api.SocialMediaApi.dto.auth.JWTAuthResponse;
import restful.api.SocialMediaApi.dto.user.UserDTO;
import restful.api.SocialMediaApi.dto.user.UserGetDTO;
import restful.api.SocialMediaApi.exceptions.AuthenticationException;
import restful.api.SocialMediaApi.exceptions.UserNotFoundException;
import restful.api.SocialMediaApi.mappers.UserMapper;
import restful.api.SocialMediaApi.exceptions.RegistrationException;
import restful.api.SocialMediaApi.models.User;
import restful.api.SocialMediaApi.repositories.SubscribeRepository;
import restful.api.SocialMediaApi.repositories.UserRepository;
import restful.api.SocialMediaApi.security.JwtUtil;
import restful.api.SocialMediaApi.security.UserDetails;
import restful.api.SocialMediaApi.validators.UserValidator;

import java.time.LocalDateTime;
import java.util.*;

@Service

public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final static Logger LOGGER = LogManager.getLogger(UserService.class);
    private final SubscribeRepository subscribeRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper,
                       UserValidator userValidator,
                       JwtUtil jwtUtil, AuthenticationManager authenticationManager, SubscribeRepository subscribeRepository, SubscribeRepository subscribeRepository1) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userValidator = userValidator;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.subscribeRepository = subscribeRepository1;
    }

    public List<UserDTO> findAll() {
        //Достаем List с user'ами из репозитория
        List<User> users = userRepository.findAll();
        //Маппим лист юзеров в лист UserResponseDTO и возвращаем
        return users.stream().map(userMapper::toUserDTO).toList();
    }

    public UserGetDTO findById(Long id) {
        User toUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with this id not found"));
        User fromUser = getUserFromContext();
        if (toUser.equals(fromUser)) {
            return new UserGetDTO(userMapper.toUserDTO(toUser));
        }
        UserGetDTO userGetDTO = new UserGetDTO(userMapper.toUserDTO(toUser), "To message this user you must be friends");

        //проверяем наличие подписки
        subscribeRepository.findByToUserAndFromUser(toUser, fromUser).ifPresent(subscribe -> {
            //если подписка двусторонняя (дружба) -> выводим сообщение о том, что пользователь может писать сообщение
            if (subscribe.isMutual()) {
                userGetDTO.setMessage("You can write message to this User");
            }
        });

        return userGetDTO;
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

    public JWTAuthResponse login(RegistrationRequest registrationRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(registrationRequest.getUsername(), registrationRequest.getPassword());
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException ex) {
            throw new AuthenticationException("Invalid username or password");
        }
        User user = findByEmail(registrationRequest.getEmail()).orElse(null);
        JWTAuthResponse JWTAuthResponse = userMapper.toJWTAuthResponse(registrationRequest);

        String token = jwtUtil.generateToken(registrationRequest.getUsername(), registrationRequest.getEmail());

        JWTAuthResponse.setToken(token);
        JWTAuthResponse.setId(user.getId());

        return JWTAuthResponse;
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private User getUserFromContext() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }
}
