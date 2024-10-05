package com.product.managemet.dto;

import java.io.Serializable;

import com.product.managemet.util.Constants;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements Serializable {

	private static final long serialVersionUID = -3999761826730114842L;

	private Long id;

    @NotBlank(message = Constants.PRODUCT_NAME_NOT_BLANK)
	@Size(min = 2, max = 100, message = Constants.PRODUCT_NAME_CHARACTER_COUNT)
    private String name;

    @NotNull(message = Constants.PRICE_NOT_BLANK)
	@DecimalMin(value = "0.0", inclusive = false, message = Constants.PRICE_MINIMUM)
    private Double price;

    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;

}
