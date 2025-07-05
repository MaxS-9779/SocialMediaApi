package restful.api.SocialMediaApi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import restful.api.SocialMediaApi.dto.subscribe.SubscribeDTO;
import restful.api.SocialMediaApi.entity.Subscribe;

import java.util.Date;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = Date.class)
public abstract class SubscribeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "mutual", ignore = true)
    public abstract Subscribe toSubscribe(SubscribeDTO subscribeDTO);

    public abstract SubscribeDTO toDTO(Subscribe subscribe);
}
