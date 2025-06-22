package restful.api.SocialMediaApi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;


@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username")
    @NotNull(message = "Username should not be empty")
    @Size(min = 2, max = 50, message = "Username must be more than 2 characters and less than 50")
    private String username;

    @Column(name = "password")
    @NotNull(message = "Password should not be empty")
    private String password;

    @Column(name = "user_email")
    @NotNull(message = "Email should not be empty")
    @Email
    private String email;

    @Column(name = "created_at")
    private Date creationDate;
}
