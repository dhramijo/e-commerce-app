package co.uk.jdreamer.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.uk.jdreamer.model.persistence.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

/**
 * This custom class is responsible for the authentication process,
 * and extends the UsernamePasswordAuthenticationFilter class,
 * which is available under both spring-security-web and spring-boot-starter-web dependency.
 * The Base class parses the user credentials (username and a password).
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    /**
     *  This method performs actual authentication by parsing (also called filtering) the user credentials.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            User credentials = new ObjectMapper().readValue(req.getInputStream(), User.class);
            log.info("Attempting authentication for user {}", credentials.getUsername());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            new ArrayList<>()));
        } catch (IOException e) {
            log.error("Authentication attempt failed: ", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * This method is originally present in the parent of the Base class.
     * After overriding, this method will be called after a user logs in successfully.
     * Below, it is generating a String token (JWT) for this user.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {
        String token = JWT.create()
                .withSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(HMAC512(SecurityConstants.SECRET.getBytes()));
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        log.info("User {} authenticated, JWT issued", ((User) auth.getPrincipal()).getUsername());
    }


    /**
     * This method is originally present in the parent of the Base class.
     * After overriding, this method will be called after a user logs in with incorrect credentials.
     * Below, it is generating a String token (JWT) for this user.
     */
    @Override
    protected void unsuccessfulAuthentication(javax.servlet.http.HttpServletRequest request,
                                              javax.servlet.http.HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, javax.servlet.ServletException {
        log.error("Authentication attempt failed. {}.", failed.getMessage());
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
