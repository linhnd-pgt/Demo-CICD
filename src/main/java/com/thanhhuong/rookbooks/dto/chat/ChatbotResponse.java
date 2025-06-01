package com.thanhhuong.rookbooks.dto.chat;

import com.thanhhuong.rookbooks.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatbotResponse {
    private String message;
    private List<Book> recommendedBooks;
}
