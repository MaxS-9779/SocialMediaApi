package restful.api.SocialMediaApi.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import restful.api.SocialMediaApi.models.User;
import restful.api.SocialMediaApi.repositories.UserRepository;
import restful.api.SocialMediaApi.services.UserService;

public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("Email", "duplicate", "This email already exists");
        }
    }
}
