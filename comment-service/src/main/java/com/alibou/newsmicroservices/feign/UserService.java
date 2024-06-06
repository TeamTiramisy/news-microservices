package com.alibou.newsmicroservices.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user-service", url = "${application.config.user-url}")
public interface UserService {

    @GetMapping("/{id}")
    UserDto findUserById(@PathVariable Integer id);
}
