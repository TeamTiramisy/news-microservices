package com.alibou.newsmicroservices.mapper;

import com.alibou.newsmicroservices.dto.CommentDto;
import com.alibou.newsmicroservices.dto.CommentUpdateDto;
import com.alibou.newsmicroservices.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDto mapToDto(Comment comment);

    Comment mapToUpdate(@MappingTarget Comment comment, CommentUpdateDto commentUpdateDto);

}