package restful.api.SocialMediaApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restful.api.SocialMediaApi.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
