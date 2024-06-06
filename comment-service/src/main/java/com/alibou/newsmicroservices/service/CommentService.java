package com.alibou.newsmicroservices.service;

import com.alibou.newsmicroservices.dto.CommentCreateDto;
import com.alibou.newsmicroservices.dto.CommentDto;
import com.alibou.newsmicroservices.dto.CommentUpdateDto;
import com.alibou.newsmicroservices.entity.Comment;
import com.alibou.newsmicroservices.exception.ResourceNotFoundException;
import com.alibou.newsmicroservices.feign.NewDto;
import com.alibou.newsmicroservices.feign.NewsService;
import com.alibou.newsmicroservices.feign.UserDto;
import com.alibou.newsmicroservices.feign.UserService;
import com.alibou.newsmicroservices.kafka.CommentNotification;
import com.alibou.newsmicroservices.kafka.CommentProducer;
import com.alibou.newsmicroservices.kafka.Modification;
import com.alibou.newsmicroservices.mapper.CommentMapper;
import com.alibou.newsmicroservices.repository.CommentRepository;
import com.alibou.newsmicroservices.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final NewsService newsService;
    private final CommentMapper commentMapper;
    private final CommentProducer commentProducer;

    public List<CommentDto> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable).stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    public CommentDto findById(Integer id){
        return commentRepository.findById(id)
                .map(commentMapper::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.FIELD_NAME_ID, id, Constant.ERROR_CODE));
    }

    @Transactional
    public CommentDto save(CommentCreateDto commentCreateDto){
        UserDto user = userService.findUserById(commentCreateDto.getUserId());
        NewDto news = newsService.findNewsById(commentCreateDto.getNewsId());

        CommentDto commentDto = Optional.of(create(user.id(), news.id(), commentCreateDto.getText()))
                .map(commentRepository::save)
                .map(commentMapper::mapToDto)
                .orElseThrow();

        commentProducer.sendNewsNotification(
                CommentNotification.builder()
                        .id(commentDto.getId())
                        .text(commentDto.getText())
                        .modification(Modification.CREATE)
                        .build()
        );

        return commentDto;

    }

    @Transactional
    public CommentDto update(Integer id, CommentUpdateDto commentUpdateDto){
        CommentDto commentDto = commentRepository.findById(id)
                .map(comment -> commentMapper.mapToUpdate(comment, commentUpdateDto))
                .map(commentRepository::saveAndFlush)
                .map(commentMapper::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.FIELD_NAME_ID, id, Constant.ERROR_CODE));

        commentProducer.sendNewsNotification(
                CommentNotification.builder()
                        .id(commentDto.getId())
                        .text(commentDto.getText())
                        .modification(Modification.UPDATE)
                        .build()
        );

        return commentDto;
    }

    @Transactional
    public boolean delete(Integer id){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.FIELD_NAME_ID, id, Constant.ERROR_CODE));

        commentRepository.delete(comment);

        commentProducer.sendNewsNotification(
                CommentNotification.builder()
                        .id(comment.getId())
                        .text(comment.getText())
                        .modification(Modification.DELETE)
                        .build()
        );

        return true;
    }

    public List<CommentDto> findAllByNewsId(Integer newsId) {
        return commentRepository.findAllByNewsId(newsId).stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    private Comment create(Integer userId, Integer newsId, String text){
        return Comment.builder()
                .text(text)
                .userId(userId)
                .newsId(newsId)
                .build();
    }
}
