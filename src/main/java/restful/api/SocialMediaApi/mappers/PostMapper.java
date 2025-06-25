package restful.api.SocialMediaApi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import restful.api.SocialMediaApi.dto.post.PostDTO;
import restful.api.SocialMediaApi.models.Post;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {
    PostDTO toDTO(Post post);

    @Mapping(target = "id", ignore = true)
    Post toPost(PostDTO postDTO);
}
