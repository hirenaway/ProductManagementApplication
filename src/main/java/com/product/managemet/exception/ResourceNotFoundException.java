package com.product.managemet.exception;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8940413553223994935L;

	public ResourceNotFoundException(String message) {
        super(message);
    }

}
