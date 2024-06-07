package com.alibou.gateway.config;

import com.alibou.gateway.dto.TokenDto;
import com.alibou.gateway.dto.UserDto;
import com.alibou.gateway.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                final String jwt;
                final String username;
                final String role;

                try {
                    jwt = authHeader.substring(7);
                    username = jwtService.extractUsername(jwt);
                    role = jwtService.extractRole(jwt);
                } catch (Exception exception) {
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                }

                if (username != null) {
                    UserDto userDto = restTemplate.getForObject("http://localhost:8080/api/v1/users/user/" + username, UserDto.class);
                    TokenDto tokenDto = restTemplate.getForObject("http://localhost:8089/api/v1/token/" + jwt, TokenDto.class);

                    boolean isTokenValid = !tokenDto.expired && !tokenDto.revoked;

                    if (!(jwtService.isTokenValid(jwt, userDto) && isTokenValid)) {
                        return onError(exchange, HttpStatus.UNAUTHORIZED);
                    }
                }

                String requiredRole = getRequiredRole(exchange.getRequest().getURI().getPath(), exchange.getRequest().getMethod());
                if (requiredRole != null && !role.equals(requiredRole)) {
                    return onError(exchange, HttpStatus.FORBIDDEN);
                }
            }
            return chain.filter(exchange);
        });
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getRequiredRole(String path, HttpMethod method) {
        if (path.startsWith("/api/v1/users")) {
            return "ADMIN";
        } else if (path.startsWith("/api/v1/news")) {
            if (method == HttpMethod.GET) {
                return null;
            } else {
                return "ADMIN";
            }
        }
        return null;
    }

    public static class Config {

    }
}
