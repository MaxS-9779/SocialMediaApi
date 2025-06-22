package restful.api.SocialMediaApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import restful.api.SocialMediaApi.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);

    public User findByUsername(String username);
}
