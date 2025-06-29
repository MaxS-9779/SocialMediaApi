package restful.api.SocialMediaApi.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import restful.api.SocialMediaApi.dto.SubscribeDTO;
import restful.api.SocialMediaApi.exceptions.SubscribeNotFoundException;
import restful.api.SocialMediaApi.exceptions.SubscribeValidateException;
import restful.api.SocialMediaApi.exceptions.UserNotFoundException;
import restful.api.SocialMediaApi.mappers.SubscribeMapper;
import restful.api.SocialMediaApi.models.Subscribe;
import restful.api.SocialMediaApi.models.User;
import restful.api.SocialMediaApi.repositories.SubscribeRepository;
import restful.api.SocialMediaApi.repositories.UserRepository;
import restful.api.SocialMediaApi.security.UserDetails;
import restful.api.SocialMediaApi.validators.SubsribeValidator;
import restful.api.SocialMediaApi.validators.UserValidator;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubscribeService {
    private final SubscribeRepository subscribeRepository;
    private final SubscribeMapper subscribeMapper;
    private final UserRepository userRepository;
    private final SubsribeValidator subsribeValidator;

    public List<SubscribeDTO> findAllSubscribes() {
        List<Subscribe> subscribes = subscribeRepository.findAll();
        return subscribes.stream().map(subscribeMapper::toDTO).collect(Collectors.toList());
    }

    public List<SubscribeDTO> findSubscribesByUserId(Long id) {
        User toUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with this id not found"));

        List<Subscribe> subscribes = subscribeRepository.findByToUser(toUser);

        return subscribes.stream().map(subscribeMapper::toDTO).collect(Collectors.toList());

    }

    @Transactional
    public String createSubscribe(Long id, BindingResult bindingResult) {
        Subscribe subscribe = new Subscribe();
        User toUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with this id not found"));

        User fromUser = getUserFromContext();

        subscribe.setToUser(toUser);
        subscribe.setFromUser(fromUser);

        subsribeValidator.validate(subscribe, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new SubscribeValidateException("Current user already subscribed at this user");
        } else {

            subscribe.setCreationDate(LocalDateTime.now());

            subscribeRepository.save(subscribe);
            return String.format("User %s subscribed to user %s", fromUser.getUsername(), toUser.getUsername());
        }
    }

    @Transactional
    public SubscribeDTO deleteSubscribe(Long id) {

        User toUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with this id not found"));

        Subscribe subscribe = subscribeRepository.findByToUserAndFromUser(toUser, getUserFromContext()).orElseThrow(() -> new SubscribeNotFoundException("Current user not subscribed to this user"));
        subscribeRepository.delete(subscribe);
        return subscribeMapper.toDTO(subscribe);
    }

    private User getUserFromContext() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }
}
