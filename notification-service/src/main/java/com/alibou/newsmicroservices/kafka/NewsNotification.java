package com.alibou.newsmicroservices.kafka;

import lombok.Builder;

@Builder
public record NewsNotification(Integer id,
                               String title,
                               Modification modification) {

}
