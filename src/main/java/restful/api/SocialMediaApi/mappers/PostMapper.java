package restful.api.SocialMediaApi.mappers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import restful.api.SocialMediaApi.dto.user.UserDTO;
import restful.api.SocialMediaApi.dto.post.PostDTO;
import restful.api.SocialMediaApi.dto.post.PostResponseDTO;
import restful.api.SocialMediaApi.models.Post;
import restful.api.SocialMediaApi.models.User;
//import restful.api.SocialMediaApi.repositories.UserRepository;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class PostMapper {
//    private final UserRepository userRepository;
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

//    @Named("userIdToUser")
//    protected User userIdToUser(Long userId){
//        return userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("User with this id does not exist"));
//    }
//
//    @Named("userToUserId")
//    protected Long userToUserId(User user){
//        return user.getId();
//    }

    @Named("userToResponseUserDTO")
    protected UserDTO userToResponseUserDTO(User user){
        return userMapper.toUserDTO(user);
    }
}
