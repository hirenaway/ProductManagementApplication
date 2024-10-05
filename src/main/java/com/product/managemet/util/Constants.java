package com.product.managemet.util;

public class Constants {

    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // Key words
    public static final String PRODUCT_KEY_WORD = "Product";
    public static final String CATEGORY_KEY_WORD = "Category";
    public static final String ORDER_KEY_WORD = "Order";
    
    // Events
    public static final String CREATE = "Create";
    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    // Error messages
    public static final String PRODUCT_NOT_FOUND = "Product not found with ID: ";
    public static final String CATEGORY_NOT_FOUND = "Category not found with ID: ";
    public static final String ORDER_NOT_FOUND = "Order not found with ID: ";

    // Validation messages
    public static final String PRODUCT_NAME_NOT_BLANK = "Product name cannot be blank";
    public static final String PRODUCT_NAME_CHARACTER_COUNT = "Product name must be between 2 and 100 characters";
    public static final String CATEGORY_NAME_CHARACTER_COUNT = "Category name must be between 3 to 15 characters";
    public static final String CATEGORY_NAME_NOT_BLANK = "Category name cannot be blank";
    public static final String ORDER_EMPTY_PRODUCT_ID = "Order must contain at least one product";
    public static final String PRICE_NOT_BLANK = "Price cannot be null";
    public static final String PRICE_MINIMUM = "Price must be greater than 0";
    
    // Product Rest end point responses
    public static final String REST_END_POINT_CREATE_PRODUCT_RESPONSE = "Product creation message sent to queue successfully";
    public static final String REST_END_POINT_UPDATE_PRODUCT_RESPONSE = "Product updation message sent to queue successfully";
    public static final String REST_END_POINT_DELETE_PRODUCT_RESPONSE = "Product deletion message sent to queue successfully";

    // Product queues
    public static final String PRODUCT_CREATE_QUEUE = "product.create.queue";
    public static final String PRODUCT_UPDATE_QUEUE = "product.update.queue";
    public static final String PRODUCT_DELETE_QUEUE = "product.delete.queue";
    
    // Category Rest end point responses
    public static final String REST_END_POINT_CREATE_CATEGORY_RESPONSE = "Category creation message sent to queue successfully";
    public static final String REST_END_POINT_UPDATE_CATEGORY_RESPONSE = "Category updation message sent to queue successfully";
    public static final String REST_END_POINT_DELETE_CATEGORY_RESPONSE = "Category deletion message sent to queue successfully";

    // Category queues
    public static final String CATEGORY_CREATE_QUEUE = "category.create.queue";
    public static final String CATEGORY_UPDATE_QUEUE = "category.update.queue";
    public static final String CATEGORY_DELETE_QUEUE = "category.delete.queue";
    
    // Order Rest end point responses
    public static final String REST_END_POINT_CREATE_ORDER_RESPONSE = "Order creation message sent to queue successfully";
    public static final String REST_END_POINT_UPDATE_ORDER_RESPONSE = "Order updation message sent to queue successfully";
    public static final String REST_END_POINT_DELETE_ORDER_RESPONSE = "Order deletion message sent to queue successfully";

    // Order queues
    public static final String ORDER_CREATE_QUEUE = "order.create.queue";
    public static final String ORDER_UPDATE_QUEUE = "order.update.queue";
    public static final String ORDER_DELETE_QUEUE = "order.delete.queue";

}
