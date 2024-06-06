package com.alibou.newsmicroservices.repository;

import com.alibou.newsmicroservices.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Integer> {
}
