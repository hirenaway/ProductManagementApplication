package com.product.managemet.dto;

import java.io.Serializable;
import java.util.List;

import com.product.managemet.util.Constants;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO implements Serializable {
	
	private static final long serialVersionUID = 2359564342241104501L;

	private Long id;

	@NotBlank(message = Constants.CATEGORY_NAME_NOT_BLANK)
	@Size(min = 3, max = 15, message = Constants.CATEGORY_NAME_CHARACTER_COUNT)
    private String name;

    private List<Long> productIds;

}
