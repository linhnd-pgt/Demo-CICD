package com.thanhhuong.rookbooks.controller.rest.impl;

import com.thanhhuong.rookbooks.controller.rest.ICommentController;
import com.thanhhuong.rookbooks.controller.rest.base.VsResponseUtil;
import com.thanhhuong.rookbooks.dto.CommentDto;
import com.thanhhuong.rookbooks.entity.Comment;
import com.thanhhuong.rookbooks.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentControllerImpl implements ICommentController {

    CommentService commentService;

    @Override
    public ResponseEntity<?> getCommentHistory() {
        return VsResponseUtil.ok(HttpStatus.OK, commentService.getAll());
    }


    @MessageMapping("/comment")
    @SendTo("/blog/comments")
    public Comment sendComment(CommentDto comment) {
        return commentService.saveComment(comment.getContent(), comment.getBlogId(), comment.getUserId());
    }

}
