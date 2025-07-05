package restful.api.SocialMediaApi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import restful.api.SocialMediaApi.dto.post.PostDTO;
import restful.api.SocialMediaApi.dto.post.PostResponseDTO;
import restful.api.SocialMediaApi.dto.user.UserDTO;
import restful.api.SocialMediaApi.entity.Post;
import restful.api.SocialMediaApi.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class PostMapper {
    @Autowired
    private UserMapper userMapper;

    public abstract PostDTO toDTO(Post post);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "image", ignore = true)
    public abstract Post toPost(PostDTO postDTO);

    @Mapping(target = "userDTO", source = "user", qualifiedByName = "userToResponseUserDTO")
    public abstract PostResponseDTO toPostResponseDTO(Post post);

    @Named("userToResponseUserDTO")
    protected UserDTO userToResponseUserDTO(User user){
        return userMapper.toUserDTO(user);
    }
}
