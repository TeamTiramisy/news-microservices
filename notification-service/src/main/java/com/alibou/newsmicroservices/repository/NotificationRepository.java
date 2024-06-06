package com.alibou.newsmicroservices.repository;

import com.alibou.newsmicroservices.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
