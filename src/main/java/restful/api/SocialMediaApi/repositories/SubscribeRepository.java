package restful.api.SocialMediaApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restful.api.SocialMediaApi.entity.Subscribe;
import restful.api.SocialMediaApi.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    List<Subscribe> findByToUser(User toUser);

    List<Subscribe> findByFromUser(User fromUser);

    Optional<Subscribe> findByToUserAndFromUser(User toUser, User fromUser);
}
