package restful.api.SocialMediaApi.repositories;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restful.api.SocialMediaApi.models.Subscribe;
import restful.api.SocialMediaApi.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    List<Subscribe> findByToUser(User toUser);

    Optional<Subscribe> findByToUserAndFromUser(User toUser, User fromUser);
}
