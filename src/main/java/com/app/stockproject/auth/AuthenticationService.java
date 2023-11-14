package com.app.stockproject.auth;

import com.app.stockproject.bean.Role;
import com.app.stockproject.bean.User;
import com.app.stockproject.dao.UserDao;
import com.app.stockproject.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        try {
            var user = User.builder()
                    .name(request.getName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
            userDao.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception e) {
            logger.error("Error occurred during user registration", e);
            throw new RuntimeException("User registration failed");
        }
    }
    public AuthenticationResponse registerAdmin(RegisterRequest request) {
        try {
            var user = User.builder()
                    .name(request.getName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ADMIN)
                    .build();
            userDao.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception e) {
            logger.error("Error occurred during user registration", e);

            throw new RuntimeException("User registration failed");
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var user = userDao.findByEmail(request.getEmail()).orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user: {}", request.getEmail(), e);
            // Handle authentication failure, e.g., throw a custom exception or return an error response.
            throw new RuntimeException("Authentication failed");
        } catch (Exception e) {
            logger.error("Error occurred during user authentication", e);
            // Handle other exceptions appropriately.
            throw new RuntimeException("Authentication failed");
        }
    }
}
