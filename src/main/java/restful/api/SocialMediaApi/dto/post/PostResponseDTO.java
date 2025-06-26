package restful.api.SocialMediaApi.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restful.api.SocialMediaApi.dto.auth.UserResponseDTO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostResponseDTO {
    private Long id;

    private String header;

    private String body;

    private UserResponseDTO userResponseDTO;
}
