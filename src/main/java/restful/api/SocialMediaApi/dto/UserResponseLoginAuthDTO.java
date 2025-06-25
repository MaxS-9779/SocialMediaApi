package restful.api.SocialMediaApi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseLoginAuthDTO {
    private Long id;
    private String username;
    private String email;
    private String token;
}
