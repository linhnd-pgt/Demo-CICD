package com.thanhhuong.rookbooks.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {
    private Long bookId;
    private String coverImage;
    private String title;
    private Double price;
    private Integer quantity;

    public double getSubtotal() {
        return price * quantity;
    }
}
