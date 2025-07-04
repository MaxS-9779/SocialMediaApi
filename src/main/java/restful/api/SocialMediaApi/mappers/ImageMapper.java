package restful.api.SocialMediaApi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import restful.api.SocialMediaApi.dto.image.ImageDTO;
import restful.api.SocialMediaApi.models.Image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ImageMapper {

    @Mapping(target = "name", source = ".", qualifiedByName = "getImageName")
    @Mapping(target = "size", source = ".", qualifiedByName = "getImageSize")
    @Mapping(target = "contentType", source = ".", qualifiedByName = "getImageContentType")
    public abstract ImageDTO toImageDTO(Image image);

    @Named("getImageName")
    protected String getImageName(Image image) {
        return Path.of(image.getPath()).getFileName().toString();
    }

    @Named("getImageSize")
    protected Long getImageSize(Image image) throws IOException {
        return Files.size(Path.of(image.getPath()));
    }

    @Named("getImageContentType")
    protected String getImageContentType(Image image) throws IOException {
        return Files.probeContentType(Path.of(image.getPath()));
    }
}
