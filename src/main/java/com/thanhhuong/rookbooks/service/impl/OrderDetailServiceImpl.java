package com.thanhhuong.rookbooks.service.impl;

import com.thanhhuong.rookbooks.entity.Order;
import com.thanhhuong.rookbooks.entity.OrderDetail;
import com.thanhhuong.rookbooks.service.OrderDetailService;
import com.thanhhuong.rookbooks.repository.OrderDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetail> getAllOrderDetailByOrder(Order order) {
        return orderDetailRepository.findByOrder(order);
    }
}
