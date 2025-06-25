package restful.api.SocialMediaApi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import restful.api.SocialMediaApi.dto.user.UserDTO;
import restful.api.SocialMediaApi.dto.auth.RegistrationLoginDTO;
import restful.api.SocialMediaApi.dto.auth.UserResponseDTO;
import restful.api.SocialMediaApi.models.User;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    User toUser(UserDTO userDTO);

    UserDTO toDTO(User user);

    @Mapping(target = "token", ignore = true)
    RegistrationLoginDTO toUserResponseLoginAuthDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "token", ignore = true)
    RegistrationLoginDTO toUserResponseLoginAuthDTO(UserDTO userDTO);

    UserResponseDTO toUserResponseDTO(User user);

    default List<Map<String, String>> toUserResponseDTOMap(List<UserResponseDTO> users) {
        return users.stream().map(user -> Map.of(
                        "Id: ", String.valueOf(user.getId()),
                        "Email: ", user.getEmail(),
                        "Username: ", user.getUsername()))
                .toList();
    }
}
