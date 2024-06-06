package com.alibou.microservices.service;

import com.alibou.microservices.dto.TokenDto;
import com.alibou.microservices.mapper.TokenMapper;
import com.alibou.microservices.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final TokenMapper tokenMapper;

    @Transactional
    public TokenDto findByToken(String token) {
        return tokenRepository.findByToken(token)
                .map(tokenMapper::mapToDto).orElseThrow();
    }
}
