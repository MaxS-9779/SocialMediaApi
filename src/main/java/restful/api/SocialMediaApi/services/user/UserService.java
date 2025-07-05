package restful.api.SocialMediaApi.services.user;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restful.api.SocialMediaApi.dto.user.UserDTO;
import restful.api.SocialMediaApi.dto.user.UserResponseDTO;
import restful.api.SocialMediaApi.entity.User;
import restful.api.SocialMediaApi.exceptions.EntityNotFoundException;
import restful.api.SocialMediaApi.mappers.UserMapper;
import restful.api.SocialMediaApi.repositories.SubscribeRepository;
import restful.api.SocialMediaApi.repositories.UserRepository;
import restful.api.SocialMediaApi.security.JwtUtil;
import restful.api.SocialMediaApi.util.AuthenticatedUser;
import restful.api.SocialMediaApi.validators.UserValidator;

import java.util.List;

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

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
