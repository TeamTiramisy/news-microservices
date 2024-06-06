package com.alibou.newsmicroservices.mapper;

import com.alibou.newsmicroservices.dto.UserCreateDto;
import com.alibou.newsmicroservices.dto.UserDto;
import com.alibou.newsmicroservices.entity.User;
import com.alibou.newsmicroservices.mapper.annotation.EncodedMapping;
import com.alibou.newsmicroservices.mapper.annotation.PasswordEncoderMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public interface UserMapper {

    UserDto mapToDto(User user);

    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    User mapToEntity(UserCreateDto userCreateDto);

    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User mapToUpdate(@MappingTarget User user, UserCreateDto userCreateDto);
}
