package restful.api.SocialMediaApi.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import restful.api.SocialMediaApi.dto.user.UserDTO;
import restful.api.SocialMediaApi.dto.auth.RegistrationLoginDTO;
import restful.api.SocialMediaApi.dto.user.UserResponseDTO;
import restful.api.SocialMediaApi.exceptions.AuthenticationException;
import restful.api.SocialMediaApi.exceptions.UserNotFoundException;
import restful.api.SocialMediaApi.mappers.UserMapper;
import restful.api.SocialMediaApi.exceptions.RegistrationException;
import restful.api.SocialMediaApi.models.User;
import restful.api.SocialMediaApi.repositories.UserRepository;
import restful.api.SocialMediaApi.security.JwtUtil;
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

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper,
                       UserValidator userValidator,
                       JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userValidator = userValidator;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public List<UserResponseDTO> findAll() {
        //Достаем List с user'ами из репозитория
        List<User> users = userRepository.findAll();
        //Маппим лист юзеров в лист UserResponseDTO и возвращаем
        return users.stream().map(userMapper::toUserResponseDTO).toList();
    }

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User with this id not found"));
        return userMapper.toUserResponseDTO(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public RegistrationLoginDTO save(UserDTO userDTO, BindingResult bindingResult) {
        LOGGER.info("Saving userDTO: {},{},{}", userDTO.getPassword(), userDTO.getUsername(), userDTO.getEmail());

        User user = userMapper.toUser(userDTO);
        LOGGER.info("Saving user: {},{},{}", user.getPassword(), user.getUsername(), user.getEmail());
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

            RegistrationLoginDTO registrationLoginDTO = userMapper.toUserResponseLoginAuthDTO(user);
            registrationLoginDTO.setToken(jwtUtil.generateToken(user.getUsername(), user.getEmail()));

            return registrationLoginDTO;
        }
    }

    public RegistrationLoginDTO login(UserDTO userDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException ex) {
            throw new AuthenticationException("Invalid username or password");
        }
        User user = findByEmail(userDTO.getEmail()).orElse(null);
        RegistrationLoginDTO registrationLoginDTO = userMapper.toUserResponseLoginAuthDTO(userDTO);

        String token = jwtUtil.generateToken(userDTO.getUsername(), userDTO.getEmail());

        registrationLoginDTO.setToken(token);
        registrationLoginDTO.setId(user.getId());

        return registrationLoginDTO;
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
