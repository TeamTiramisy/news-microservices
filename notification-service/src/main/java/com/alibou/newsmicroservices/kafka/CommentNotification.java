package com.alibou.newsmicroservices.kafka;

import lombok.Builder;

@Builder
public record CommentNotification(Integer id,
                                  String text,
                                  Modification modification) {
}
