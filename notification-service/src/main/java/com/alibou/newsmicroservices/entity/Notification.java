package com.alibou.newsmicroservices.entity;

import com.alibou.newsmicroservices.kafka.CommentNotification;
import com.alibou.newsmicroservices.kafka.NewsNotification;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Notification {

    @Id
    private String id;
    private NotificationType type;
    private LocalDateTime notificationDate;
    private NewsNotification newsNotification;
    private CommentNotification commentNotification;
}
