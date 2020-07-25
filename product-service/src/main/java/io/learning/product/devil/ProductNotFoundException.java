package io.learning.product.devil;

public class ProductNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Throwable th) {
        super(message, th);
    }

}
