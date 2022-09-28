package it.develhope.controllerprotection;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.develhope.controllerprotection.auth.service.LoginService;
import it.develhope.controllerprotection.user.entities.User;
import it.develhope.controllerprotection.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepo;

    private Collection<? extends GrantedAuthority> getAuthorities(User user){
        if (user == null || !user.isActive()) return List.of();
        return user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.getName())).toList();
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String token = header.trim();

        DecodedJWT decoded;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(LoginService.JWT_SECRET)).withIssuer("develhope-demo").build();
            decoded = verifier.verify(token);
        }catch (JWTVerificationException ex){
            chain.doFilter(request, response);
            return;
        }

        // Get user identity and set it on the spring security context
        Optional<User> userDetails = userRepo.findById(decoded.getClaim("id").asLong());
        if (!userDetails.isPresent() || !userDetails.get().isActive()) {
            chain.doFilter(request, response);
            return;
        }

        User user = userDetails.get();
        user.setPassword(null);
        user.setActivationCode(null);
        user.setPasswordResetCode(null);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user, null, List.of()
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
