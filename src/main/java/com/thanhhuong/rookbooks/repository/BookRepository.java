package com.thanhhuong.rookbooks.repository;

import com.thanhhuong.rookbooks.entity.Book;
import com.thanhhuong.rookbooks.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findByTitleContainingAndActiveFlag(String keyword, boolean activeFlag, Pageable pageable);

    Book findByTitleAndActiveFlag(String title, boolean activeFlag);

    Page<Book> findByCategoryAndActiveFlag(Category category, boolean activeFlag, Pageable pageable);

    Page<Book> findByCategoryIdAndOriginalPriceBetweenAndActiveFlag(Long categoryId, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Book> findByCategory_IdAndTitleContainingAndOriginalPriceBetweenAndActiveFlag(Long categoryId, String keyword, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Book> findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderBySalePriceDesc(Long categoryId, String keyword, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Book> findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderBySalePriceAsc(Long categoryId, String keyword, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Book> findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtAsc(Long categoryId, String keyword, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Book> findByCategoryIdAndTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtDesc(Long categoryId, String keyword, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Book> findByTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtAsc(String title, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Book> findByTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderByCreatedAtDesc(String title, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Book> findByTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderBySalePriceAsc(String title, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Book> findByTitleContainingAndOriginalPriceBetweenAndActiveFlagOrderBySalePriceDesc(String title, double minPrice, double maxPrice, boolean activeFlag, Pageable pageable);

    Page<Book> findAllByOrderByIdDesc(Pageable pageable);

    List<Book> findTop4ByActiveFlagOrderByBuyCountDesc(boolean activeFlag);

    List<Book> findByActiveFlagOrderByCreatedAtDesc(boolean activeFlag);

    List<Book> findTop10ByActiveFlagOrderByTotalRevenueDesc(boolean activeFlag);

    List<Book> findAllByActiveFlag(boolean activeFlag);

    List<Book> findAllByCategoryAndActiveFlag(Category category, boolean activeFlag);

    Page<Book> findByCategoryIdAndActiveFlag(Long categoryId, boolean activeFlag, Pageable pageable);

    Page<Book> findByCategory_IdAndTitleContainingAndActiveFlag(Long categoryId, String keyword, boolean activeFlag, Pageable pageable);

    @Query(value = "SELECT b.title, SUM(od.price * od.quantity) AS total_revenue FROM books b " +
            "JOIN order_details od ON b.id = od.book_id " +
            "JOIN orders o ON od.order_id = o.id " +
            "WHERE MONTH(o.create_date) = :month " +
            "GROUP BY b.title " +
            "ORDER BY total_revenue " +
            "DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> findTop10BestSellerByMonth(@Param("month") int month);

    @Query("SELECT MONTH(od.createdAt) AS month, SUM(od.totalPrice) AS totalRevenue " +
            "FROM Order od " +
            "WHERE YEAR(od.createdAt) = :year " +
            "GROUP BY MONTH(od.createdAt) " +
            "ORDER BY MONTH(od.createdAt)")
    List<Object[]> findMonthlyRevenue(@Param("year") int year);

    List<Book> findTop4ByCategoryIdAndActiveFlag(Long id, boolean b);
}