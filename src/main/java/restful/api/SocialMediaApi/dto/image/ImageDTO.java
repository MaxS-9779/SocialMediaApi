package restful.api.SocialMediaApi.dto.image;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность изображения")
public class ImageDTO {
    @Schema(description = "Название изображения")
    private String name;
    @Schema(description = "Размер изображения")
    private Long size;
    @Schema(description = "Тип изображения")
    private String contentType;
}
