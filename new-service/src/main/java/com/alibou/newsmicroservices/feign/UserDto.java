package com.alibou.newsmicroservices.feign;


public record UserDto(Integer id,
                      String username,
                      String firstname,
                      String lastname) {

}
