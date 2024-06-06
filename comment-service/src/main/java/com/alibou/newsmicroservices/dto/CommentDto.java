package com.alibou.newsmicroservices.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class CommentDto {

    Integer id;

    LocalDateTime date;

    String text;

    Integer newsId;

    Integer userId;
}
