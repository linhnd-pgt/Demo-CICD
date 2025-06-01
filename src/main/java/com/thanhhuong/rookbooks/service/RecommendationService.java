package com.thanhhuong.rookbooks.service;

import com.thanhhuong.rookbooks.entity.Book;

import java.util.List;

public interface RecommendationService {

    List<Book> getRecommendationsForUser(Long userId, int limit);

    List<Book> getSimilarBooks(Long bookId, int limit);

}
