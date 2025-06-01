package com.thanhhuong.rookbooks.service;

import com.thanhhuong.rookbooks.dto.CartDTO;
import com.thanhhuong.rookbooks.dto.CartItemDTO;
import jakarta.servlet.http.HttpSession;

public interface CartService {
    void addToCart(HttpSession session, CartItemDTO cartItem);

    void updateCartItemQuantity(HttpSession session, Long productId, int quantity);

    void removeCartItem(HttpSession session, Long productId);

    void clearCart(HttpSession session);

    CartDTO getCart(HttpSession session);
}

