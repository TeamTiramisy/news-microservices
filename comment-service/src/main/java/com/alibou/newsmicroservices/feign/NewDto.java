package com.alibou.newsmicroservices.feign;

import lombok.Value;

import java.time.LocalDateTime;

public record NewDto(Integer id,
                     LocalDateTime date,
                     String title,
                     String text,
                     Integer userId) {
}
