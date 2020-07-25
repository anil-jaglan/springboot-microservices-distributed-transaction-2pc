package io.learning.order.devil;

public class InSufficientFundException extends RuntimeException {

    private static final long serialVersionUID = 5029135185567404773L;

    public InSufficientFundException(String message) {
        super(message);
    }

    public InSufficientFundException(String message, Throwable th) {
        super(message, th);
    }
}
