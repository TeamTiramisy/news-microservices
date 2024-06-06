package com.alibou.newsmicroservices.controller;

import com.alibou.newsmicroservices.dto.CommentCreateDto;
import com.alibou.newsmicroservices.dto.CommentDto;
import com.alibou.newsmicroservices.dto.CommentUpdateDto;
import com.alibou.newsmicroservices.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import jakarta.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("api/v1/comments")
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> findAll(Pageable pageable){
        return new ResponseEntity<>(commentService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> findById(@PathVariable @Positive Integer id){
        return new ResponseEntity<>(commentService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommentDto> save(@RequestBody @Validated CommentCreateDto commentCreateDto, @RequestHeader("Authorization") String token){
        return new ResponseEntity<>(commentService.save(commentCreateDto, token), HttpStatus.CREATED);
    }

    @GetMapping("/news/{id}")
    public ResponseEntity<List<CommentDto>> findAllByNews(@PathVariable @Positive Integer id){
        return new ResponseEntity<>(commentService.findAllByNewsId(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> update(@PathVariable @Positive Integer id,
                                             @RequestBody @Validated CommentUpdateDto commentUpdateDto){
        return new ResponseEntity<>(commentService.update(id, commentUpdateDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable @Positive Integer id) {
        commentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
