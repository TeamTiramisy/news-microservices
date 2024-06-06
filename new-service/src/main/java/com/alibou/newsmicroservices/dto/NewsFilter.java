package com.alibou.newsmicroservices.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NewsFilter {

    String title;

    String text;

}
