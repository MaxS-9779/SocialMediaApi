package restful.api.SocialMediaApi.util;

import org.mapstruct.Mapper;
import restful.api.SocialMediaApi.dto.UserDTO;
import restful.api.SocialMediaApi.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);
}
