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
public class UserResponseDTO {
    private UserDTO user;
    @Schema(description = "Сообщение о возможности отправлять пользователю сообщения")
    private String message;

    public UserResponseDTO(UserDTO user) {
        this.user = user;
    }
}
