package com.thanhhuong.rookbooks.service.impl;

import com.thanhhuong.rookbooks.entity.Book;
import com.thanhhuong.rookbooks.entity.User;
import com.thanhhuong.rookbooks.repository.BookRepository;
import com.thanhhuong.rookbooks.repository.UserRepository;
import com.thanhhuong.rookbooks.service.RecommendationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RecommendationServiceImpl implements RecommendationService {

    BookRepository bookRepository;
    UserRepository userRepository;
    @Override
    public List<Book> getRecommendationsForUser(Long userId, int limit) {
        // Get the user
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Collections.emptyList();
        }

        // Get books the user has interacted with (favorites, purchases, etc.)
        Set<Book> userBooks = new HashSet<>();

        // Add favorite books
        if (user.getFavoriteBooks() != null) {
            userBooks.addAll(user.getFavoriteBooks());
        }

        // If user hasn't interacted with any books, return popular books
        if (userBooks.isEmpty()) {
            return bookRepository.findTop4ByActiveFlagOrderByBuyCountDesc(true);
        }

        // Extract features from user's books to build a user profile
        Map<String, Integer> categoryFrequency = new HashMap<>();
        Map<String, Integer> authorFrequency = new HashMap<>();
        Map<String, Integer> publisherFrequency = new HashMap<>();

        for (Book book : userBooks) {
            // Count category occurrences
            String categoryName = book.getCategory() != null ? book.getCategory().getName() : "Unknown";
            categoryFrequency.put(categoryName, categoryFrequency.getOrDefault(categoryName, 0) + 1);

            // Count author occurrences
            String author = book.getAuthor() != null ? book.getAuthor() : "Unknown";
            authorFrequency.put(author, authorFrequency.getOrDefault(author, 0) + 1);

            // Count publisher occurrences
            String publisher = book.getPublisher() != null ? book.getPublisher() : "Unknown";
            publisherFrequency.put(publisher, publisherFrequency.getOrDefault(publisher, 0) + 1);
        }

        // Get all active books
        List<Book> allBooks = bookRepository.findAllByActiveFlag(true);

        // Filter out books the user already has
        List<Book> candidateBooks = allBooks.stream()
                .filter(book -> !userBooks.contains(book))
                .collect(Collectors.toList());

        // Calculate similarity scores for each candidate book
        Map<Book, Double> bookScores = new HashMap<>();

        for (Book candidateBook : candidateBooks) {
            double score = 0.0;

            String bookCategory = candidateBook.getCategory() != null ? candidateBook.getCategory().getName() : "Unknown";
            if (categoryFrequency.containsKey(bookCategory)) {
                score += 3.0 * categoryFrequency.get(bookCategory);
            }

            String bookAuthor = candidateBook.getAuthor() != null ? candidateBook.getAuthor() : "Unknown";
            if (authorFrequency.containsKey(bookAuthor)) {
                score += 2.0 * authorFrequency.get(bookAuthor);
            }

            // Publisher similarity (lowest weight)
            String bookPublisher = candidateBook.getPublisher() != null ? candidateBook.getPublisher() : "Unknown";
            if (publisherFrequency.containsKey(bookPublisher)) {
                score += 1.0 * publisherFrequency.get(bookPublisher);
            }

            bookScores.put(candidateBook, score);
        }

        return bookScores.entrySet().stream()
                .sorted(Map.Entry.<Book, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getSimilarBooks(Long bookId, int limit) {
        Book referenceBook = bookRepository.findById(bookId).orElse(null);
        if (referenceBook == null) {
            return Collections.emptyList();
        }

        List<Book> allBooks = bookRepository.findAllByActiveFlag(true).stream()
                .filter(book -> !book.getId().equals(bookId))
                .collect(Collectors.toList());

        Map<Book, Double> similarityScores = new HashMap<>();

        for (Book book : allBooks) {
            double score = 0.0;

            if (referenceBook.getCategory() != null && book.getCategory() != null &&
                    referenceBook.getCategory().getId().equals(book.getCategory().getId())) {
                score += 3.0;
            }

            if (referenceBook.getAuthor() != null && book.getAuthor() != null &&
                    referenceBook.getAuthor().equals(book.getAuthor())) {
                score += 2.0;
            }

            if (referenceBook.getPublisher() != null && book.getPublisher() != null &&
                    referenceBook.getPublisher().equals(book.getPublisher())) {
                score += 1.0;
            }

            if (referenceBook.getSalePrice() != null && book.getSalePrice() != null) {
                double priceDiff = Math.abs(referenceBook.getSalePrice() - book.getSalePrice());
                double maxPrice = Math.max(referenceBook.getSalePrice(), book.getSalePrice());
                if (priceDiff / maxPrice < 0.2) { // If price difference is less than 20%
                    score += 0.5;
                }
            }

            similarityScores.put(book, score);
        }

        return similarityScores.entrySet().stream()
                .sorted(Map.Entry.<Book, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
