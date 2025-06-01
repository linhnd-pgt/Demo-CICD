package com.thanhhuong.rookbooks.dto;

import lombok.Data;

@Data
public class CommentDto {

    String content;

    Long blogId;

    Long userId;

}
