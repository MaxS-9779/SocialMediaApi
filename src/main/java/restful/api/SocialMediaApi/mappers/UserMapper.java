package restful.api.SocialMediaApi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import restful.api.SocialMediaApi.dto.auth.JWTAuthResponse;
import restful.api.SocialMediaApi.dto.auth.RegistrationRequest;
import restful.api.SocialMediaApi.dto.user.UserDTO;
import restful.api.SocialMediaApi.models.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    public abstract User toUser(RegistrationRequest registrationRequest);

    public abstract RegistrationRequest toRegistrationRequest(User user);

    @Mapping(target = "token", ignore = true)
    public abstract JWTAuthResponse toJWTAuthResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "token", ignore = true)
    public abstract JWTAuthResponse toJWTAuthResponse(RegistrationRequest registrationRequest);

    public abstract UserDTO toUserDTO(User user);
}
