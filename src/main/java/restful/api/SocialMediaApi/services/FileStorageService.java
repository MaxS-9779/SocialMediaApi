package restful.api.SocialMediaApi.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import restful.api.SocialMediaApi.dto.image.ImageDTO;
import restful.api.SocialMediaApi.models.Image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    @Value("${app.upload.dir}")
    private String fileStorageLocation;

    public void saveFile(MultipartFile file) throws IOException {

        byte[] bytes = file.getBytes();

        Path path = Paths.get(fileStorageLocation + file.getOriginalFilename());
        Files.write(path, bytes);
    }

    public Image getImage(MultipartFile file) {

        Path path = Paths.get(fileStorageLocation + file.getOriginalFilename());

        return new Image(path.toString());
    }

    public ImageDTO getImageDTO(MultipartFile file) {
        return new ImageDTO(
                file.getOriginalFilename(),
                file.getSize(),
                file.getContentType());
    }

    public static void deleteFile(Image image) throws IOException {
        Path path = Paths.get(image.getPath());
        Files.delete(path);
    }
}
