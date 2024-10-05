package com.product.managemet.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateMessage implements Serializable{

	private static final long serialVersionUID = 694865001893506782L;

	private Long id;
    private ProductDTO productDTO;

}
