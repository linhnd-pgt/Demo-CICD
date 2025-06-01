package com.thanhhuong.rookbooks.service;

import com.thanhhuong.rookbooks.dto.BookDto;
import com.thanhhuong.rookbooks.dto.CategoryDto;
import com.thanhhuong.rookbooks.dto.OrderDTO;
import com.thanhhuong.rookbooks.entity.User;

import java.util.List;

public interface ExportService {

    String exportOrderReport(User user, List<OrderDTO> orderDTOList, String keyword);

    String exportCategoryReport(User user, List<CategoryDto> categoryDTOList, String keyword);

    String exportBookReport(User user, List<BookDto> bookDtoList, String keyword);

}
