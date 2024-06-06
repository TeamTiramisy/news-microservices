package com.alibou.microservices.feign;

import com.alibou.microservices.dto.UserCreateDto;
import com.alibou.microservices.dto.UserDto;
import jakarta.validation.groups.Default;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${application.config.user-url}")
public interface UserService {

    @GetMapping("/user/{username}")
    UserDto findUserByUsername(@PathVariable String username);

    @PostMapping
    UserDto save(@RequestBody UserCreateDto userCreateDto);
}
