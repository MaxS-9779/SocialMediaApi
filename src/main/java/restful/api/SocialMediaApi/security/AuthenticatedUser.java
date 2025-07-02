package restful.api.SocialMediaApi.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import restful.api.SocialMediaApi.models.User;

@Component
public class AuthenticatedUser {
    public static User getUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();

        UserDetails userDetails = (UserDetails) principal;

        return userDetails.getUser();
    }
}
