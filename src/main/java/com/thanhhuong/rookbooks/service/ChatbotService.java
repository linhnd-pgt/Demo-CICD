package com.thanhhuong.rookbooks.service;

import com.thanhhuong.rookbooks.dto.chat.ChatbotResponse;
import com.thanhhuong.rookbooks.entity.Book;

import java.util.List;

public interface ChatbotService {
    ChatbotResponse processMessage(String message);

    List<Book> searchBooksByKeywords(List<String> keywords);
}
