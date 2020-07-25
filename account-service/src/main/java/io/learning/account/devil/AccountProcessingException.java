package io.learning.account.devil;

public class AccountProcessingException extends RuntimeException {

    private static final long serialVersionUID = 8337796345425999746L;

    public AccountProcessingException(String message) {
        super(message);
    }

    public AccountProcessingException(String message, Throwable th) {
        super(message, th);
    }

}
