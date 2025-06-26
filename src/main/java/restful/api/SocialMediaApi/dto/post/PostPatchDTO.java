package restful.api.SocialMediaApi.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Сущность поста, возвращаемая при обновлении поста, содержит краткую информацию о посте")
public class PostPatchDTO {
    @Schema(description = "Заголовок поста", example = "About animals")
    private String header;

    @NotBlank(message = "Post body cannot be empty")
    @Schema(description = "Тело поста", example = "Some interesting text about animals")
    private String body;
}
