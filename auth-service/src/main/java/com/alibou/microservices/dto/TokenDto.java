package com.alibou.microservices.dto;

import com.alibou.microservices.entity.TokenType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TokenDto {
    Integer id;

    String token;

    TokenType tokenType = TokenType.BEARER;

    boolean revoked;

    boolean expired;

    Integer userId;
}
