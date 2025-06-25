package restful.api.SocialMediaApi.dto.post;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostDTO {
    @NotBlank(message = "Post header cannot be empty")
    @Size(min = 5, max = 100, message = "Post header must be more than 5 characters and less than 100")
    private String header;

    @NotBlank(message = "Post body cannot be empty")
    private String body;
}
