package restful.api.SocialMediaApi.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 2, max = 50, message = "Username must be more than 2 characters and less than 50")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 5, max = 50, message = "Password must be more than 5 characters and less than 50")
    private String password;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;
}
