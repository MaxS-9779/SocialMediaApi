package restful.api.SocialMediaApi.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Сущность, возвращаемая при регистрации и аутентификации")
public class JWTAuthResponse {

    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "Petya Ivanov")
    private String username;

    @Schema(description = "Email пользователя", example = "PetyaIvanov2015@mail.ru")
    private String email;

    @Schema(description = "JWT Токен пользователя", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJVc2VyIGluZm8iLCJ1c2VybmFtZSI6InVzZXIyNSIsImlhdCI6MTc1MDkzMTQ2MCwiaXNzIjoiU29jaWFsTWVkaWFBcGkiLCJleHAiOjE3NTE1MzYyNjB9.lBc2RwPswYQEYu2FnW_fluNwuobyZCDpR-u_CqY0zpU")
    private String token;
}
