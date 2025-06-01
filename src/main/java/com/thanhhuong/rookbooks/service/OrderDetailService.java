package com.thanhhuong.rookbooks.service;

import com.thanhhuong.rookbooks.entity.Order;
import com.thanhhuong.rookbooks.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> getAllOrderDetailByOrder(Order order);
}
