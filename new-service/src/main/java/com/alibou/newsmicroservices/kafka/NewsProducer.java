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
public class NewsProducer {

    private final KafkaTemplate<String, NewsNotification> kafkaTemplate;

    public void sendNewsNotification(NewsNotification newsNotification) {
        log.info("Sending news notification");
        Message<NewsNotification> message = MessageBuilder
                .withPayload(newsNotification)
                .setHeader(TOPIC, "news-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
