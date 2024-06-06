package com.alibou.gateway.config;

import com.alibou.gateway.dto.TokenDto;
import com.alibou.gateway.dto.UserDto;
import com.alibou.gateway.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;
    private final JwtService jwtService;
    private final RestTemplate restTemplate;

    public AuthenticationFilter(RouteValidator validator, JwtService jwtService, RestTemplate restTemplate) {
        super(Config.class);
        this.validator = validator;
        this.jwtService = jwtService;
        this.restTemplate = restTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }


                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new RuntimeException("missing authorization header");
                }

                String jwt = authHeader.substring(7);
                String username = jwtService.extractUsername(jwt);

                if (username != null) {
                    UserDto userDto = restTemplate.getForObject("http://localhost:8080/api/v1/users/user/" + username, UserDto.class);
                    TokenDto tokenDto = restTemplate.getForObject("http://localhost:8089/api/v1/token/" + jwt, TokenDto.class);

                    boolean isTokenValid = tokenDto.expired && tokenDto.revoked;

                    if (jwtService.isTokenValid(jwt, userDto) && isTokenValid) {
                        throw new RuntimeException("invalid token");
                    }
                } else {
                    throw new RuntimeException("invalid token");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
