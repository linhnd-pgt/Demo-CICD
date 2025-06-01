package com.thanhhuong.rookbooks.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest {
    private Long productId;
    private int quantity;
}
