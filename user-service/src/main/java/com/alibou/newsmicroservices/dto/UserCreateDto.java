package com.alibou.newsmicroservices.dto;

import com.alibou.newsmicroservices.validation.CreateAction;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import com.alibou.newsmicroservices.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateDto {

    @Email
    @NotBlank
    String username;

    @Size(min = 3, max = 12)
    @NotBlank(groups = CreateAction.class)
    String password;

    @NotBlank
    String firstname;

    @NotBlank
    String lastname;

    Role role;
}
