package restful.api.SocialMediaApi.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность, возвращаемая при GET запросе по адресу /users/{id}")
public class UserGetDTO {
    private UserDTO user;
    private String message;

    public UserGetDTO(UserDTO user) {
        this.user = user;
    }
}
