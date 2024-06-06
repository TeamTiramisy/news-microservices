package com.alibou.microservices.service;

import com.alibou.microservices.dto.AuthenticationRequest;
import com.alibou.microservices.dto.AuthenticationResponse;
import com.alibou.microservices.dto.UserCreateDto;
import com.alibou.microservices.dto.UserDto;
import com.alibou.microservices.entity.Token;
import com.alibou.microservices.entity.TokenType;
import com.alibou.microservices.exception.PasswordException;
import com.alibou.microservices.feign.UserService;
import com.alibou.microservices.repository.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(UserCreateDto request) {
        UserCreateDto user = UserCreateDto.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .password(request.getPassword())
                .role(request.getRole())
                .build();

        UserDto userDto = userService.save(user);
        String jwtToken = jwtService.generateToken(userDto);
        String refreshToken = jwtService.generateRefreshToken(userDto);
        saveUserToken(userDto, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
      UserDto user = userService.findUserByUsername(request.getUsername());

        boolean isCorrectPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (isCorrectPassword) {
            String jwtToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        throw new PasswordException("Incorrect password");
    }

    private void revokeAllUserTokens(UserDto user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());

        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setRevoked(true);
                token.setExpired(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }

    private void saveUserToken(UserDto user, String jwtToken) {
        Token token = Token.builder()
                .userId(user.getId())
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);

        if (username != null) {
            UserDto user = userService.findUserByUsername(username);

            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
