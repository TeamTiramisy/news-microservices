package com.alibou.newsmicroservices.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class NewsDto {

    Integer id;

    LocalDateTime date;

    String title;

    String text;

    Integer userId;

}
