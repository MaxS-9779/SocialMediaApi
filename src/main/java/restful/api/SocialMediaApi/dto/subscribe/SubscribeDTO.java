package restful.api.SocialMediaApi.dto.subscribe;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restful.api.SocialMediaApi.dto.user.UserDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность, содержащая краткую информацию о подписке и пользователей")
public class SubscribeDTO {
    private UserDTO toUser;

    private UserDTO fromUser;
}
