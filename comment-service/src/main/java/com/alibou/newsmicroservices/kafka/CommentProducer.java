package com.alibou.newsmicroservices.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentProducer {

    private final KafkaTemplate<String, CommentNotification> kafkaTemplate;

    public void sendNewsNotification(CommentNotification commentNotification) {
        log.info("Sending comment notification");
        Message<CommentNotification> message = MessageBuilder
                .withPayload(commentNotification)
                .setHeader(TOPIC, "comment-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
