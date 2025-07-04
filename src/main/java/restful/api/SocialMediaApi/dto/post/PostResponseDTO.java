package restful.api.SocialMediaApi.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restful.api.SocialMediaApi.dto.image.ImageDTO;
import restful.api.SocialMediaApi.dto.user.UserDTO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Сущность поста, возвращаемая при просмотре содержит краткую информацию о посте и пользователе, создавшем пост")
public class PostResponseDTO {
    @Schema(description = "Id поста", example = "1")
    private Long id;

    @Schema(description = "Заголовок поста", example = "About animals")
    private String header;

    @Schema(description = "Тело поста", example = "Some interesting text about animals")
    private String body;

    private ImageDTO image;

    @Schema(description = "Сущность, возвращаемая при запросе поста, содержит краткую информацию о пользователе")
    private UserDTO userDTO;
}
