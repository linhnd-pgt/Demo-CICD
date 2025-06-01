package com.thanhhuong.rookbooks.service;

import com.thanhhuong.rookbooks.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getAll();
    Comment saveComment(String content, Long blogId, Long userId);

    List<Comment> findByBlogIdOrderByCreatedAtDesc(Long blogId);
}
