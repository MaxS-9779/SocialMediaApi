package restful.api.SocialMediaApi.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationLoginDTO {
    private Long id;
    private String username;
    private String email;
    private String token;
}
