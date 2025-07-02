package restful.api.SocialMediaApi.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restful.api.SocialMediaApi.models.Post;
import restful.api.SocialMediaApi.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByBody(String body);

    Optional<Post> findByHeader(String header);

    User user(User user);

    List <Post> findByUserIdIn(List<Long> userId, Pageable pageable);

    List <Post> findByUserIdIn(List<Long> userId, Sort sort);
}
