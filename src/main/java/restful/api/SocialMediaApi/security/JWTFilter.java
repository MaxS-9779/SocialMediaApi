package restful.api.SocialMediaApi.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import restful.api.SocialMediaApi.services.UserDetailService;


import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailService userDetailsService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        if (requestUri.startsWith("/auth/") ||
            requestUri.startsWith("/swagger-ui/") ||
            requestUri.startsWith("/v3/api-docs/") ||
            requestUri.startsWith("/swagger-resources/") ||
            requestUri.startsWith("/webjars/")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            var authorizationHeader = request.getHeader("Authorization");

            if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
                sendError(response, "Missing or invalid Authorization header");
                return;
            }

            String token = authorizationHeader.substring(7);

            if (token.isBlank()) {
                sendError(response, "Invalid JWT token: token is blank");
                return;
            }

            try {
                String username = jwtUtil.verifyTokenAndReturnUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities());

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (AuthorizationDeniedException | JWTVerificationException ex) {
                sendError(response, "Invalid JWT token: " + ex.getMessage());
                return;
            }

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            sendError(response, "Authentication failed: " + ex.getMessage());
        }
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
        response.getWriter().flush();
    }
}


