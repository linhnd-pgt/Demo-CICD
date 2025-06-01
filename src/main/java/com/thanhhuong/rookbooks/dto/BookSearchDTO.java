package com.thanhhuong.rookbooks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookSearchDTO {
    private Long categoryId;
    private String keyword;

}

