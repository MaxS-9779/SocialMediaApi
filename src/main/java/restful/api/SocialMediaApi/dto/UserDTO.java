package restful.api.SocialMediaApi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    @NotNull(message = "Username should not be empty")
    @Size(min = 2, max = 50, message = "Username must be more than 2 characters and less than 50")
    private String username;

    @NotNull(message = "Password should not be empty")
    private String password;

    @NotNull(message = "Email should not be empty")
    @Email
    private String email;
}
