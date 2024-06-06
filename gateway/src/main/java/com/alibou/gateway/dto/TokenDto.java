package com.alibou.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    public Integer id;


    public String token;


    public TokenType tokenType;

    public boolean revoked;

    public boolean expired;

    public Integer userId;
}
