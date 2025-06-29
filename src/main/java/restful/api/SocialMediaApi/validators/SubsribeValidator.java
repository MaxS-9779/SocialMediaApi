package restful.api.SocialMediaApi.validators;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import restful.api.SocialMediaApi.models.Subscribe;
import restful.api.SocialMediaApi.repositories.SubscribeRepository;

@Component
@AllArgsConstructor
public class SubsribeValidator implements Validator {
    private final SubscribeRepository subscribeRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Subscribe.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Subscribe subscribe = (Subscribe) target;
        if (subscribeRepository.findByToUserAndFromUser(subscribe.getToUser(), subscribe.getFromUser()).isPresent()){
            errors.rejectValue("toUser", "duplicate", "This subscribe already exists");
        }
    }
}
