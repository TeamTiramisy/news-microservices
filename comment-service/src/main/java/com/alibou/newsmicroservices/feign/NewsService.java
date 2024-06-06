package com.alibou.newsmicroservices.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "new-service", url = "${application.config.news-url}")
public interface NewsService {

    @GetMapping("/{id}")
    NewDto findNewsById(@PathVariable Integer id);
}
