package com.alibou.microservices.controller;

import com.alibou.microservices.dto.TokenDto;
import com.alibou.microservices.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/{token}")
    public ResponseEntity<TokenDto> findByToken(@PathVariable String token) {
        return ResponseEntity.ok(tokenService.findByToken(token));
    }
}
