package com.thanhhuong.rookbooks.service;

import com.thanhhuong.rookbooks.entity.Book;
import com.thanhhuong.rookbooks.dto.BookDto;
import com.thanhhuong.rookbooks.dto.BookSearchDTO;
import com.thanhhuong.rookbooks.dto.MonthlyRevenueDTO;
import com.thanhhuong.rookbooks.dto.UserSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface BookService {

    List<Book> findAll();

    List<Book> findAllActive();

    void addBook(Book book, MultipartFile coverImage) throws IOException;

    void editBook(Book book, MultipartFile coverImage) throws IOException;

    void deleteBook(Long id) throws Exception;

    void setActiveFlag(Long id, boolean activeFlag) throws Exception;

    Book getBookById(Long id);

    Book getBookByName(String name);

    Page<Book> searchBooks(BookSearchDTO search, Pageable pageable);

    Page<Book> searchBooksUser(UserSearchDTO search, Pageable pageable);

    Page<Book> getAllBooksForUsers(Pageable pageable);

    List<Book> getTop4BestSeller();

    List<Book> getTop10BestSeller();

    List<BookDto> getTop10BestSellerByMonth(int month);

    List<MonthlyRevenueDTO> getMonthRevenuePerYear(int year);

    List<Book> findAllOrderByCreatedDate();

    Set<Book> getFavoriteBooksByUserId(Long id);

    Long countBook();

    List<Book> getAllBooksByCategoryId(Long id);

    Page<Book> getAllBooksPaginatedByCategoryId(Long categoryId, Pageable pageable);


}
