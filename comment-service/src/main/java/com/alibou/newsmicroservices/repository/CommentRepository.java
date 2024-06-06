package com.alibou.newsmicroservices.repository;

import com.alibou.newsmicroservices.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByNewsId(Integer newsId);
}
