package restful.api.SocialMediaApi.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Запрос на регистрацию")
public class RegistrationRequest {
    @Schema(description = "Имя пользователя", example = "Petya Ivanov")
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 2, max = 50, message = "Username must be more than 2 characters and less than 50")
    private String username;

    @Schema(description = "Email пользователя", example = "PetyaIvanov2015@mail.ru")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Пароль пользователя", example = "123456qwerty")
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 5, max = 50, message = "Password must be more than 5 characters and less than 50")
    private String password;
}
