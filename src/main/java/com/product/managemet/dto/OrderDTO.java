package com.product.managemet.dto;

import java.io.Serializable;
import java.util.Set;

import com.product.managemet.util.Constants;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO implements Serializable {
	
	private static final long serialVersionUID = -6626727461980774200L;

	private Long id;

    @NotEmpty(message = Constants.ORDER_EMPTY_PRODUCT_ID)
    private Set<Long> productIds;

}
