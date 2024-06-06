package com.alibou.newsmicroservices.controller;

import com.alibou.newsmicroservices.dto.NewsCreateDto;
import com.alibou.newsmicroservices.dto.NewsDto;
import com.alibou.newsmicroservices.dto.NewsFilter;
import com.alibou.newsmicroservices.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Positive;

import java.util.List;

@RestController
@RequestMapping("api/v1/news")
@RequiredArgsConstructor
@Validated
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<List<NewsDto>> findAll(NewsFilter newsFilter, Pageable pageable) {
        return new ResponseEntity<>(newsService.findAll(newsFilter, pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsDto> findById(@PathVariable @Positive Integer id) {
        return new ResponseEntity<>(newsService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<NewsDto> save(@RequestBody @Validated NewsCreateDto newsCreateDto, @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(newsService.save(newsCreateDto, token), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<NewsDto> update(@PathVariable @Positive Integer id,
                                          @RequestBody @Validated NewsCreateDto newsCreateDto,
                                          @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(newsService.update(id, newsCreateDto, token), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable @Positive Integer id) {
        newsService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
