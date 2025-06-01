package com.thanhhuong.rookbooks.service.impl;

import com.thanhhuong.rookbooks.constant.OrderStatus;
import com.thanhhuong.rookbooks.constant.PaymentMethod;
import com.thanhhuong.rookbooks.dto.CartDTO;
import com.thanhhuong.rookbooks.dto.CartItemDTO;
import com.thanhhuong.rookbooks.dto.OrderPerson;
import com.thanhhuong.rookbooks.entity.Book;
import com.thanhhuong.rookbooks.entity.Order;
import com.thanhhuong.rookbooks.entity.OrderDetail;
import com.thanhhuong.rookbooks.entity.User;
import com.thanhhuong.rookbooks.repository.BookRepository;
import com.thanhhuong.rookbooks.service.OrderService;
import com.thanhhuong.rookbooks.repository.OrderDetailRepository;
import com.thanhhuong.rookbooks.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;


@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private BookRepository bookRepository;
    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Page<Order> getAllOrdersOnPage(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public void setProcessingOrder(Order order) {
        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);
    }

    @Override
    public void setDeliveringOrder(Order order) {
        order.setStatus(OrderStatus.DELIVERING);
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrder(order);
        for (OrderDetail orderDetail : orderDetailList) {
            orderDetail.getBook().setBuyCount(orderDetail.getBook().getBuyCount() + orderDetail.getQuantity());
            orderDetail.getBook().setQty(orderDetail.getBook().getQty() - orderDetail.getQuantity());
            orderDetailRepository.save(orderDetail);
        }

        orderRepository.save(order);
    }

    @Override
    public void setReceivedToOrder(Order order) {
        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
    }

    @Override
    public void setUnpaidOrder(Order order, String vnpTxnRef) {
        order.setStatus(OrderStatus.UNPAID);
        order.setPaymentMethod(PaymentMethod.VNPAY);
        order.setVnpTxnRef(vnpTxnRef);
        orderRepository.save(order);
    }

    @Override
    public Order getOrderByVnpTxnRef(String vnpTxnRef) {
        return orderRepository.findByVnpTxnRef(vnpTxnRef).orElse(null);
    }


    @Override
    public List<Order> getTop10orders() {
        return orderRepository.findTop10ByOrderByCreatedAtDesc();
    }

    @Override
    public BigDecimal getTotalRevenue() {
        return orderRepository.sumTotalPrice();
    }

    @Override
    public Long countOrder() {
        return orderRepository.count();
    }

    @Override
    public List<Order> getAllOrdersByUser(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public Order createUnpaidOrder(CartDTO cart, User user, OrderPerson orderPerson) {
        Order order = new Order();
        order.setReciever(orderPerson.getFullName());
        order.setStatus(OrderStatus.UNPAID);
        order.setEmailAddress(orderPerson.getEmail());
        order.setShippingAddress(orderPerson.getAddress());
        order.setPhoneNumber(orderPerson.getPhoneNumber());
        order.setTotalPrice(cart.calculateTotalAmount());
        order.setPaymentMethod(PaymentMethod.COD);

        // Thêm các chi tiết đơn hàng từ giỏ hàng
        List<CartItemDTO> cartItems = cart.getCartItems();
        for (CartItemDTO cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            Book book = bookRepository.findById(cartItem.getBookId()).orElse(null);
            orderDetail.setBook(book);
            orderDetail.setQuantity(cartItem.getQuantity());
            assert book != null;
            orderDetail.setPrice(book.getSalePrice());
            order.addOrderDetail(orderDetail);
            double currentTotalRevenue = book.getTotalRevenue();
            if (book.getTotalRevenue() == 0) {
                currentTotalRevenue = (double) 0;
            }
            book.setTotalRevenue(currentTotalRevenue + (double) orderDetail.getPrice());
        }

        // Set thông tin người dùng và thời gian
        order.setUser(user);
        order.setCreatedAt(new Date());
        // Lưu đơn đặt hàng vào cơ sở dữ liệu
        return orderRepository.save(order);
    }

    public static String generateOrderCode() {
        int codeLength = 10;

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        StringBuilder code = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        String prefix = "BOOK";

        return prefix + "-" + code.toString();
    }
    @Override
    public Order createOrder(CartDTO cart, User user, OrderPerson orderPerson) {
        Order order = new Order();
        order.setReciever(orderPerson.getFullName());
        order.setStatus(OrderStatus.PENDING);
        order.setEmailAddress(orderPerson.getEmail());
        order.setShippingAddress(orderPerson.getAddress());
        order.setPhoneNumber(orderPerson.getPhoneNumber());
        order.setTotalPrice(cart.calculateTotalAmount());
        order.setPaymentMethod(PaymentMethod.COD);
        order.setCode(generateOrderCode());

        // Thêm các chi tiết đơn hàng từ giỏ hàng
        List<CartItemDTO> cartItems = cart.getCartItems();
        for (CartItemDTO cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            Book book = bookRepository.findById(cartItem.getBookId()).orElse(null);
            orderDetail.setBook(book);
            orderDetail.setQuantity(cartItem.getQuantity());
            assert book != null;
            orderDetail.setPrice(book.getSalePrice());
            order.addOrderDetail(orderDetail);
            double currentTotalRevenue = book.getTotalRevenue();
            if (book.getTotalRevenue() == 0) {
                currentTotalRevenue = (double) 0;
            }
            book.setTotalRevenue(currentTotalRevenue + (double) orderDetail.getPrice());
        }

        // Set thông tin người dùng và thời gian
        order.setUser(user);
        order.setCreatedAt(new Date());
        // Lưu đơn đặt hàng vào cơ sở dữ liệu
        return orderRepository.save(order);
    }

    @Override
    public Order createVNPayOrder(CartDTO cart, User user, OrderPerson orderPerson) {
        Order order = new Order();
        order.setReciever(orderPerson.getFullName());
        order.setStatus(OrderStatus.PENDING);
        order.setEmailAddress(orderPerson.getEmail());
        order.setShippingAddress(orderPerson.getAddress());
        order.setPhoneNumber(orderPerson.getPhoneNumber());
        order.setTotalPrice(cart.calculateTotalAmount());
        order.setPaymentMethod(PaymentMethod.VNPAY);
        order.setCode(generateOrderCode());

        // Thêm các chi tiết đơn hàng từ giỏ hàng
        List<CartItemDTO> cartItems = cart.getCartItems();
        for (CartItemDTO cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            Book book = bookRepository.findById(cartItem.getBookId()).orElse(null);
            orderDetail.setBook(book);
            orderDetail.setQuantity(cartItem.getQuantity());
            assert book != null;
            orderDetail.setPrice(book.getSalePrice());
            order.addOrderDetail(orderDetail);
            double currentTotalRevenue = book.getTotalRevenue();
            if (book.getTotalRevenue() == 0) {
                currentTotalRevenue = (double) 0;
            }
            book.setTotalRevenue(currentTotalRevenue + (double) orderDetail.getPrice());
        }

        // Set thông tin người dùng và thời gian
        order.setUser(user);
        // Lưu đơn đặt hàng vào cơ sở dữ liệu
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Order order) {
        return null;
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public void cancelOrder(Order order) {
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public Page<Order> getOrdersByStatus(String status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable);
    }

    @Override
    public void updateUnpaidToPending(Order order) {
        order.setStatus(OrderStatus.PENDING);
    }

}
