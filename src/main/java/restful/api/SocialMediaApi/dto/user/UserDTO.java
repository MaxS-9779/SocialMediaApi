package restful.api.SocialMediaApi.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Сущность, содержащая краткую информацию о пользователе")
public class UserDTO {

    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "Petya Ivanov")
    private String username;

    @Schema(description = "Email пользователя", example = "PetyaIvanov2015@mail.ru")
    private String email;
}


