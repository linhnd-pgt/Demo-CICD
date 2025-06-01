package com.thanhhuong.rookbooks.service.impl;

import com.thanhhuong.rookbooks.constant.SortType;
import com.thanhhuong.rookbooks.dto.BookDto;
import com.thanhhuong.rookbooks.dto.BookSearchDTO;
import com.thanhhuong.rookbooks.dto.MonthlyRevenueDTO;
import com.thanhhuong.rookbooks.dto.UserSearchDTO;
import com.thanhhuong.rookbooks.entity.Book;
import com.thanhhuong.rookbooks.entity.Category;
import com.thanhhuong.rookbooks.entity.OrderDetail;
import com.thanhhuong.rookbooks.entity.User;
import com.thanhhuong.rookbooks.repository.BookRepository;
import com.thanhhuong.rookbooks.repository.CategoryRepository;
import com.thanhhuong.rookbooks.repository.OrderDetailRepository;
import com.thanhhuong.rookbooks.repository.UserRepository;
import com.thanhhuong.rookbooks.service.BookService;
import com.thanhhuong.rookbooks.service.CategoryService;
import com.thanhhuong.rookbooks.service.FileUploadService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookServiceImpl implements BookService {

    BookRepository bookRepository;
    CategoryRepository categoryRepository;
    UserRepository userRepository;
    CategoryService categoryService;
    FileUploadService fileUploadService;
    OrderDetailRepository orderDetailRepository;

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findAllActive() {
        return bookRepository.findAllByActiveFlag(true);
    }

    @Override
    public void addBook(Book book, MultipartFile coverImage) throws IOException {
        Book savedBook = bookRepository.save(book);
        savedBook.setCoverImage(fileUploadService.uploadFile(coverImage));
        bookRepository.save(book);
    }

    @Override
    public void editBook(Book book, MultipartFile coverImage) throws IOException {
        Book savedBook = bookRepository.save(book);
        if (!coverImage.isEmpty()) {
            savedBook.setCoverImage(fileUploadService.uploadFile(coverImage));
            bookRepository.save(book);
        }
    }

    @Override
    public void deleteBook(Long id) throws Exception {
        List<OrderDetail> orderDetailsFindByBookId = orderDetailRepository.findByBookId((id));
        if(!orderDetailsFindByBookId.isEmpty()){
            throw new Exception("Sách đã có trong các đơn hàng , vui lòng xoá các đơn hàng có sách trước");
        }
        bookRepository.deleteById(id);
    }

    @Override
    public void setActiveFlag(Long id, boolean activeFlag) throws Exception {
        Book bookById = bookRepository.findById(id).orElse(null);
        if(bookById == null){
            throw new Exception("Không tìm thấy sách với id này");
        }
        bookById.setActiveFlag(activeFlag);
        bookRepository.save(bookById);
    }

    @Override
    public Book getBookById(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        return bookOptional.orElse(null);
    }

    @Override
    public Book getBookByName(String name) {
        return bookRepository.findByTitleAndActiveFlag(name, true);
    }
    private String generateUniqueFileName(String originalFileName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return timestamp + "_" + originalFileName;
    }


    @Override
    public Page<Book> searchBooks(BookSearchDTO search, Pageable pageable) {
        Long categoryId = search.getCategoryId();
        String keyword = search.getKeyword();

        // Lấy dữ liệu phân trang dựa trên categoryId, keyword và các điều kiện tìm kiếm khác (nếu có)
        if (categoryId != null && keyword != null) {
            return bookRepository.findByCategory_IdAndTitleContainingAndActiveFlag(categoryId, keyword, true, pageable);
        } else if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            return bookRepository.findByCategoryAndActiveFlag(category, true, pageable);

        } else if (keyword != null) {
            return bookRepository.findByTitleContainingAndActiveFlag(keyword, true, pageable);
        } else {
            return bookRepository.findAllByOrderByIdDesc(pageable);
        }
    }

    @Override
    public Page<Book> searchBooksUser(UserSearchDTO search, Pageable pageable) {
        Long categoryId = search.getCategoryId();
        String keyword = search.getKeyword();
        if (keyword == null) keyword = "";
        String sortBy = search.getSortBy();
        String amountGap = search.getAmountGap();
        String[] temp = amountGap.split(" ");
        Double startAmount = Double.parseDouble(temp[0].replace(".", ""));
        Double endAmount = Double.parseDouble(temp[3].replace(".", ""));


        Page<Book> booksPage = bookRepository.findAll(pageable);
        if (categoryId != null) {
            if (sortBy.equals(SortType.oldest)) {
                booksPage = bookRepository.findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtAsc(categoryId, keyword, startAmount, endAmount, true, pageable);
            } else if (sortBy.equals(SortType.newest)) {
                booksPage = bookRepository.findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtDesc(categoryId, keyword, startAmount, endAmount, true, pageable);

            } else if (sortBy.equals(SortType.priceLowToHigh)) {
                booksPage = bookRepository.findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderBySalePriceAsc(categoryId, keyword, startAmount, endAmount, true, pageable);

            } else {
                booksPage = bookRepository.findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderBySalePriceDesc(categoryId, keyword, startAmount, endAmount, true, pageable);
            }
        } else {
            if (sortBy.equals(SortType.oldest)) {
                booksPage = bookRepository.findByTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtAsc(keyword, startAmount, endAmount, true, pageable);
            } else if (sortBy.equals(SortType.newest)) {
                booksPage = bookRepository.findByTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtDesc(keyword, startAmount, endAmount, true, pageable);

            } else if (sortBy.equals(SortType.priceLowToHigh)) {
                booksPage = bookRepository.findByTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderBySalePriceAsc(keyword, startAmount, endAmount, true, pageable);

            } else {
                booksPage = bookRepository.findByTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderBySalePriceDesc(keyword, startAmount, endAmount, true, pageable);
            }
        }
        if (sortBy == null) {
            booksPage = bookRepository.findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtDesc(categoryId, keyword, startAmount, endAmount, true, pageable);
        }
        return booksPage;
    }


    @Override
    public Page<Book> getAllBooksForUsers(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public List<Book> getTop4BestSeller() {
        return bookRepository.findTop4ByActiveFlagOrderByBuyCountDesc(true);
    }

    @Override
    public List<Book> getTop10BestSeller() {
        return bookRepository.findTop10ByActiveFlagOrderByTotalRevenueDesc(true);
    }

    @Override
    public List<BookDto> getTop10BestSellerByMonth(int month) {
        List<Object[]> result = bookRepository.findTop10BestSellerByMonth(month);
        List<BookDto> resultConvertedToDto = new ArrayList<>();
        for (Object[] item : result) {
            resultConvertedToDto.add(new BookDto(item[0].toString(), Double.parseDouble(item[1].toString())));
        }
        return resultConvertedToDto;
    }

    @Override
    public List<Book> findAllOrderByCreatedDate() {
        return bookRepository.findByActiveFlagOrderByCreatedAtDesc(true);
    }

    @Override
    public Set<Book> getFavoriteBooksByUserId(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return user.getFavoriteBooks();
        }
        return Collections.emptySet();
    }

    @Override
    public Long countBook() {
        return bookRepository.count();
    }

    @Override
    public List<Book> getAllBooksByCategoryId(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        return bookRepository.findAllByCategoryAndActiveFlag(category, true);
    }

    @Override
    public Page<Book> getAllBooksPaginatedByCategoryId(Long categoryId, Pageable pageable) {
        return bookRepository.findByCategoryIdAndActiveFlag(categoryId, true, pageable);
    }

    @Override
    public List<MonthlyRevenueDTO> getMonthRevenuePerYear(int year) {
        List<Object[]> result = bookRepository.findMonthlyRevenue(year);
        List<MonthlyRevenueDTO> resultConvertedToDto = new ArrayList<>();
        for (Object[] item : result) {
            resultConvertedToDto.add(new MonthlyRevenueDTO(Integer.parseInt(item[0].toString()), Double.parseDouble(item[1].toString())));
        }
        return resultConvertedToDto;
    }


}
