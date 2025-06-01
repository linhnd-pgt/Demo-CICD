package com.thanhhuong.rookbooks.repository;

import com.thanhhuong.rookbooks.entity.Order;
import com.thanhhuong.rookbooks.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserOrderByCreatedAtDesc(User user);

    Page<Order> findByStatus(String status, Pageable pageable);

    List<Order> findTop10ByOrderByCreatedAtDesc();

    @Query("SELECT SUM(o.totalPrice) FROM Order o where  o.status = 'DELIVERING'")
    BigDecimal sumTotalPrice();

    Optional<Order> findByVnpTxnRef(String vnpTxnRef);
}
