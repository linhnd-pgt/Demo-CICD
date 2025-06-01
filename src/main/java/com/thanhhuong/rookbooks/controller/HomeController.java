package com.thanhhuong.rookbooks.controller;

import com.thanhhuong.rookbooks.controller.common.BaseController;
import com.thanhhuong.rookbooks.entity.User;
import com.thanhhuong.rookbooks.service.BookService;
import com.thanhhuong.rookbooks.entity.Book;
import com.thanhhuong.rookbooks.service.CategoryService;
import com.thanhhuong.rookbooks.service.RecommendationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@AllArgsConstructor
@Controller

public class HomeController extends BaseController {

    private BookService bookService;
    private CategoryService categoryService;
    private RecommendationService recommendationService;

    @GetMapping("/")
    String getUserHomePage(Model model) {

        List<Book> top4BestSeller = bookService.getTop4BestSeller();
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("top4BestSeller", top4BestSeller);
        List<Book> newProducts = bookService.findAllOrderByCreatedDate();
        model.addAttribute("newProducts", newProducts);

        User currentUser = super.getCurrentUser();
        if (currentUser != null) {
            List<Book> recommendations = recommendationService.getRecommendationsForUser(currentUser.getId(), 8);
            model.addAttribute("recommendedBooks", recommendations);
        }

        return "user/index";
    }


}
