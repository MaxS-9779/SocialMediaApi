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
@Schema(description = "Сущность, содержащая краткую информацию о подписке")
public class SubscribeDTO {
    @Schema(description = "Пользователь осуществляющий подписку")
    private UserDTO toUser;

    @Schema(description = "Пользователь, на которого подписались")
    private UserDTO fromUser;
}
