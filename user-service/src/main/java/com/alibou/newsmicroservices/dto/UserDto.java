package com.alibou.newsmicroservices.dto;

import lombok.Builder;
import lombok.Value;
import com.alibou.newsmicroservices.entity.Role;

@Value
@Builder
public class UserDto {

    Integer id;

    String username;

    String password;

    String firstname;

    String lastname;

    Role role;
}