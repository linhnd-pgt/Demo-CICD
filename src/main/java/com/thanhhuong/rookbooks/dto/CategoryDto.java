package com.thanhhuong.rookbooks.dto;

import com.thanhhuong.rookbooks.entity.Category;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
@Value
public class CategoryDto implements Serializable {
    String name;
    Double totalRevenue;
}