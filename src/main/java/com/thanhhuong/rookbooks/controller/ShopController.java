package com.thanhhuong.rookbooks.controller;

import com.thanhhuong.rookbooks.service.BookService;
import com.thanhhuong.rookbooks.controller.common.BaseController;
import com.thanhhuong.rookbooks.dto.UserSearchDTO;
import com.thanhhuong.rookbooks.entity.Book;
import com.thanhhuong.rookbooks.entity.Category;
import com.thanhhuong.rookbooks.service.CategoryService;
import com.thanhhuong.rookbooks.service.RecommendationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/shop")
public class ShopController extends BaseController {

    private CategoryService categoryService;
    private BookService bookService;
    private RecommendationService recommendationService;

    @GetMapping
    public String getShopPage(
            @ModelAttribute("searchModel") UserSearchDTO searchModel,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            Model model) {

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        Pageable pageable = PageRequest.of(page - 1, 6);

        Page<Book> searchResult;

        if (searchModel.isEmpty()) {
            searchResult = bookService.getAllBooksForUsers(pageable);
        } else {
            searchResult = bookService.searchBooksUser(searchModel, pageable);
        }

        model.addAttribute("books", searchResult);


        model.addAttribute("totalPages", searchResult.getTotalPages());
        model.addAttribute("currentPage", searchResult.getNumber());
        model.addAttribute("sortBy", searchModel.getSortBy());
        model.addAttribute("sortBy", searchModel.getCategoryId());
        model.addAttribute("categoryId", searchModel.getCategoryId());
        model.addAttribute("amountGap", searchModel.getAmountGap());

        if(searchModel.getCategoryId() != null){
            Category category = categoryService.getCategoryById(searchModel.getCategoryId());
            model.addAttribute("categoryName", category.getName() + "'s Products");
        }
        else {
            model.addAttribute("categoryName", "All Product");
        }

        return "user/shop";
    }

    @GetMapping("/product/{product_id}")
    public String viewProductDetail(@PathVariable Long product_id, Model model) {
        Book product = bookService.getBookById(product_id);
        model.addAttribute("product", product);
        List<Book> similarBooks = recommendationService.getSimilarBooks(product_id, 4);
        model.addAttribute("similarBooks", similarBooks);
        return "user/product_details";
    }
}
