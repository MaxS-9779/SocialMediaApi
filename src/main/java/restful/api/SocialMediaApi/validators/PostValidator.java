package restful.api.SocialMediaApi.validators;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import restful.api.SocialMediaApi.exceptions.EntityNotFoundException;
import restful.api.SocialMediaApi.exceptions.ValidateException;
import restful.api.SocialMediaApi.models.Post;
import restful.api.SocialMediaApi.repositories.PostRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostValidator implements Validator {
    private final PostRepository postRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Post.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Post post = (Post) target;

        if (errors.hasErrors()) {
            throw new ValidateException("Invalid query");
        }

        if (postRepository.findByBody(post.getBody()).isPresent() && postRepository.findByHeader(post.getHeader()).isPresent()) {
            errors.rejectValue("body", "duplicate", "This post already exists");
        }
    }

    public static void bindingResultValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMsg.append(fieldError.getField())
                        .append(" - ").append(fieldError.getDefaultMessage())
                        .append(";");
            }
            throw new EntityNotFoundException(errorMsg.toString());
        }
    }
}
