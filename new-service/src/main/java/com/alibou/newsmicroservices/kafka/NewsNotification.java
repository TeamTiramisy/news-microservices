package com.alibou.newsmicroservices.kafka;

import com.alibou.newsmicroservices.feign.UserDto;
import lombok.Builder;

@Builder
public record NewsNotification(Integer id,
                               String title,
                               Modification modification) {

}
