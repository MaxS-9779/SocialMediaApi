package restful.api.SocialMediaApi.services.subscribe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import restful.api.SocialMediaApi.dto.subscribe.SubscribeDTO;
import restful.api.SocialMediaApi.exceptions.EntityNotFoundException;
import restful.api.SocialMediaApi.exceptions.ValidateException;
import restful.api.SocialMediaApi.mappers.SubscribeMapper;
import restful.api.SocialMediaApi.entity.Subscribe;
import restful.api.SocialMediaApi.entity.User;
import restful.api.SocialMediaApi.repositories.SubscribeRepository;
import restful.api.SocialMediaApi.repositories.UserRepository;
import restful.api.SocialMediaApi.util.AuthenticatedUser;
import restful.api.SocialMediaApi.validators.SubsribeValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
        User toUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with this id not found"));

        List<Subscribe> subscribes = subscribeRepository.findByToUser(toUser);

        return subscribes.stream().map(subscribeMapper::toDTO).collect(Collectors.toList());

    }

    @Transactional
    public String createSubscribe(Long id) {
        User toUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with this id not found"));
        User fromUser = AuthenticatedUser.getUserFromContext();

        //проверяем не на себя ли пытается подписаться наш пользователь
        if (toUser.equals(fromUser)) {
            throw new ValidateException("You cannot create a subscribe with yourself");
        }

        Subscribe subscribe = new Subscribe();

        subscribe.setToUser(toUser);
        subscribe.setFromUser(fromUser);

        Errors errors = new BeanPropertyBindingResult(subscribe, "subscribe");

        //проверяем не подписан ли еще пользователь на этого пользователя
        subsribeValidator.validate(subscribe, errors);
        if (errors.hasErrors()) {
            throw new ValidateException("Current user already subscribed at this user");
        }

        subscribeRepository.save(subscribe);

        setMutual(toUser, fromUser);

        return String.format("User %s subscribed to user %s", fromUser.getUsername(), toUser.getUsername());

    }

    @Transactional
    public SubscribeDTO deleteSubscribe(Long id) {

        User toUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with this id not found"));
        User fromUser = AuthenticatedUser.getUserFromContext();

        Subscribe subscribeTo = subscribeRepository.findByToUserAndFromUser(toUser, fromUser).orElseThrow(() -> new EntityNotFoundException("Current user not subscribed to this user"));

        if (subscribeTo.isMutual()){
            Subscribe subscribeFrom = subscribeRepository.findByToUserAndFromUser(fromUser,toUser).orElseThrow(() -> new EntityNotFoundException("User not subscribed to this user"));
            subscribeFrom.setMutual(false);
            subscribeRepository.save(subscribeFrom);
        }

        subscribeRepository.delete(subscribeTo);

        return subscribeMapper.toDTO(subscribeTo);
    }

    private void setMutual(User toUser, User fromUser) {
        Optional<Subscribe> optSubscribeTo = subscribeRepository.findByToUserAndFromUser(toUser, fromUser);
        Optional<Subscribe> optSubscribeFrom = subscribeRepository.findByToUserAndFromUser(fromUser, toUser);

        if (optSubscribeTo.isPresent() && optSubscribeFrom.isPresent()) {
            Subscribe subscribeTo = optSubscribeTo.get();
            Subscribe subscribeFrom = optSubscribeFrom.get();

            subscribeTo.setMutual(true);
            subscribeFrom.setMutual(true);

            subscribeRepository.save(subscribeTo);
            subscribeRepository.save(subscribeFrom);
        }
    }
}
