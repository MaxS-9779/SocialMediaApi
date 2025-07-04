package restful.api.SocialMediaApi.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restful.api.SocialMediaApi.dto.image.ImageDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Сущность поста")
public class PostDTO {
    @NotBlank(message = "Post header cannot be empty")
    @Size(min = 5, max = 100, message = "Post header must be more than 5 characters and less than 100")
    @Schema(description = "Заголовок поста", example = "About animals")
    private String header;

    @NotBlank(message = "Post body cannot be empty")
    @Schema(description = "Тело поста", example = "Some interesting text about animals")
    private String body;

    private ImageDTO image;

    public PostDTO(String header, String body) {
        this.header = header;
        this.body = body;
    }
}
