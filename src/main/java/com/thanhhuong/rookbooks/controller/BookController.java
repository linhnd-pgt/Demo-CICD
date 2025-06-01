package com.thanhhuong.rookbooks.controller;

import com.thanhhuong.rookbooks.service.BookService;
import com.thanhhuong.rookbooks.entity.Book;
import com.thanhhuong.rookbooks.service.RecommendationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookController {

    BookService bookService;
    RecommendationService recommendationService;

    @GetMapping("/sort-books")
    public ModelAndView sortBooks(@RequestParam("sortBy") String sortBy,
                                  @RequestParam("categoryId") Long categoryId,
                                  Model model){
        List<Book> bookList;
        if(categoryId != null){
            bookList = bookService.getAllBooksByCategoryId(categoryId);
        }
        else {
            bookList = bookService.findAll();
        }

        List<Book> sortedBookList;

        switch (sortBy){
            case "price-low-to-high":
                sortedBookList = bookList.stream()
                        .sorted(Comparator.comparing(Book::getSalePrice))
                        .collect(Collectors.toList());
                break;
            case "price-high-to-low":
                sortedBookList = bookList.stream()
                        .sorted(Comparator.comparing(Book::getSalePrice).reversed())
                        .collect(Collectors.toList());
                break;
            case "newest":
                sortedBookList = bookList.stream()
                        .sorted(Comparator.comparing(Book::getPublishedDate))
                        .collect(Collectors.toList());
                break;
            case "oldest":
                sortedBookList = bookList.stream()
                        .sorted(Comparator.comparing(Book::getPublishedDate).reversed())
                        .collect(Collectors.toList());
                break;
            default:
                sortedBookList = bookList;
        }
        model.addAttribute("bookList", sortedBookList);
        return new ModelAndView("fragments/bookListFragment :: bookList", model.asMap());
    }

}
