package com.alibou.newsmicroservices.service;

import com.alibou.newsmicroservices.dto.NewsCreateDto;
import com.alibou.newsmicroservices.dto.NewsDto;
import com.alibou.newsmicroservices.dto.NewsFilter;
import com.alibou.newsmicroservices.entity.News;
import com.alibou.newsmicroservices.exception.ResourceNotFoundException;
import com.alibou.newsmicroservices.feign.UserDto;
import com.alibou.newsmicroservices.feign.UserService;
import com.alibou.newsmicroservices.kafka.Modification;
import com.alibou.newsmicroservices.kafka.NewsNotification;
import com.alibou.newsmicroservices.kafka.NewsProducer;
import com.alibou.newsmicroservices.mapper.NewsMapper;
import com.alibou.newsmicroservices.repository.NewsRepository;
import com.alibou.newsmicroservices.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsService {

    private final NewsRepository newsRepository;

    private final NewsMapper newsMapper;

    private final UserService userService;

    private final NewsProducer newsProducer;

    public List<NewsDto> findAll(NewsFilter newsFilter, Pageable pageable) {
        News news = newsMapper.mapFromFilter(newsFilter);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher(Constant.FIELD_NAME_TITLE, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher(Constant.FIELD_NAME_TEXT, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        return newsRepository.findAll(Example.of(news, matcher), pageable).stream()
                .map(newsMapper::mapToDto)
                .collect(toList());
    }

    public NewsDto findById(Integer id) {
        return newsRepository.findById(id)
                .map(newsMapper::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.FIELD_NAME_ID, id, Constant.ERROR_CODE));
    }

    @Transactional
    public NewsDto save(NewsCreateDto dto, String token) {
        UserDto user = userService.findUserById(dto.getUserId(), token);

        NewsDto newsDto = Optional.of(dto)
                .map(newsMapper::mapToEntity)
                .map(news -> {
                    news.setUserId(user.id());
                    return newsRepository.save(news);
                })
                .map(newsMapper::mapToDto)
                .orElseThrow();

        newsProducer.sendNewsNotification(
                NewsNotification.builder()
                        .id(newsDto.getId())
                        .title(newsDto.getTitle())
                        .modification(Modification.CREATE)
                        .build()
        );

        return newsDto;
    }

    @Transactional
    public NewsDto update(Integer id, NewsCreateDto newsCreateDto, String token) {
        UserDto user = userService.findUserById(newsCreateDto.getUserId(), token);

        NewsDto newsDto = newsRepository.findById(id)
                .map(news -> newsMapper.mapToUpdate(news, newsCreateDto))
                .map(newsRepository::saveAndFlush)
                .map(newsMapper::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.FIELD_NAME_ID, id, Constant.ERROR_CODE));

        newsProducer.sendNewsNotification(
                NewsNotification.builder()
                        .id(newsDto.getId())
                        .title(newsDto.getTitle())
                        .modification(Modification.UPDATE)
                        .build()
        );

        return newsDto;
    }

    @Transactional
    public boolean delete(Integer id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.FIELD_NAME_ID, id, Constant.ERROR_CODE));

        newsRepository.delete(news);

        newsProducer.sendNewsNotification(
                NewsNotification.builder()
                        .id(news.getId())
                        .title(news.getTitle())
                        .modification(Modification.DELETE)
                        .build()
        );

        return true;
    }

}
