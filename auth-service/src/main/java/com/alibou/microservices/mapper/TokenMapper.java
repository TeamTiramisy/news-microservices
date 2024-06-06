package com.alibou.microservices.mapper;


import com.alibou.microservices.dto.TokenDto;
import com.alibou.microservices.entity.Token;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TokenMapper {

    TokenDto mapToDto(Token token);
}
