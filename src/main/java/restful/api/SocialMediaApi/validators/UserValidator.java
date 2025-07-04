package restful.api.SocialMediaApi.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import restful.api.SocialMediaApi.models.User;
import restful.api.SocialMediaApi.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("Email", "duplicate", "This email already exists");
        }
    }
}
