package restful.api.SocialMediaApi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(String username, String email) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusDays(7).toInstant());

        return JWT.create()
                .withSubject("User info")
                .withClaim("username", username)
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withIssuer("SocialMediaApi")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String verifyTokenAndReturnUsername(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User info")
                .withIssuer("SocialMediaApi")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}
