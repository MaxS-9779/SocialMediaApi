package restful.api.SocialMediaApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import restful.api.SocialMediaApi.responces.AuthenticateErrorResponse;
import restful.api.SocialMediaApi.responces.RegistrationErrorResponse;
import restful.api.SocialMediaApi.responces.UserNotFoundExceptionResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegistrationException.class)
    private ResponseEntity<RegistrationErrorResponse> handlerException(RegistrationException ex) {
        RegistrationErrorResponse response = new RegistrationErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    private ResponseEntity<AuthenticateErrorResponse> handlerException(AuthenticationException ex) {
        AuthenticateErrorResponse response = new AuthenticateErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<UserNotFoundExceptionResponse> handlerException(UserNotFoundException ex) {
        UserNotFoundExceptionResponse response = new UserNotFoundExceptionResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
