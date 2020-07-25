package io.learning.product.devil;

public class ProductProcessingException extends RuntimeException {

    private static final long serialVersionUID = -191255860130453309L;

    public ProductProcessingException(String message) {
        super(message);
    }

    public ProductProcessingException(String message, Throwable th) {
        super(message, th);
    }
}
