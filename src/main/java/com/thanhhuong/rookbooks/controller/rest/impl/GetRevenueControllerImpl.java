package com.thanhhuong.rookbooks.controller.rest.impl;

import com.thanhhuong.rookbooks.controller.rest.IGetRevenueController;
import com.thanhhuong.rookbooks.controller.rest.base.RestApiV1;
import com.thanhhuong.rookbooks.controller.rest.base.VsResponseUtil;
import com.thanhhuong.rookbooks.service.BookService;
import com.thanhhuong.rookbooks.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestApiV1
@AllArgsConstructor
public class GetRevenueControllerImpl implements IGetRevenueController {

    private BookService bookService;
    private CategoryService categoryService;

    @Override
    public ResponseEntity<?> getProductRevenueByMonth(@PathVariable("selectedMonth") int selectedMonth) throws UnsupportedEncodingException {
        return VsResponseUtil.ok(HttpStatus.OK, bookService.getTop10BestSellerByMonth(selectedMonth));
    }

    @Override
    public ResponseEntity<?> getMonthRevenueByYear(@PathVariable("selectedYear") int selectedYear) throws UnsupportedEncodingException {
        return VsResponseUtil.ok(HttpStatus.OK, bookService.getMonthRevenuePerYear(selectedYear));
    }

}
