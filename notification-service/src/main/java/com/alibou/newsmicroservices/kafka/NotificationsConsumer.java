package com.alibou.newsmicroservices.kafka;

import com.alibou.newsmicroservices.entity.Notification;
import com.alibou.newsmicroservices.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.alibou.newsmicroservices.entity.NotificationType.*;
import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationsConsumer {

    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "news-topic")
    public void consumeNewsNotifications(NewsNotification newsNotification) throws MessagingException {
        log.info(format("Consuming the message from news-topic Topic:: %s", newsNotification));
        notificationRepository.save(
                Notification.builder()
                        .type(NEWS_NOTIFICATION)
                        .notificationDate(LocalDateTime.now())
                        .newsNotification(newsNotification)
                        .build()
        );
    }

    @KafkaListener(topics = "comment-topic")
    public void consumeCommentNotifications(CommentNotification commentNotification) throws MessagingException {
        log.info(format("Consuming the message from comment-topic Topic:: %s", commentNotification));
        notificationRepository.save(
                Notification.builder()
                        .type(COMMENT_NOTIFICATION)
                        .notificationDate(LocalDateTime.now())
                        .commentNotification(commentNotification)
                        .build()
        );
    }
}
