package io.learning.order.devil;

public class OrderProcessingException extends RuntimeException {

    private static final long serialVersionUID = -4310868473761742309L;

    public OrderProcessingException(String message) {
        super(message);
    }

    public OrderProcessingException(String message, Throwable th) {
        super(message, th);
    }

}
