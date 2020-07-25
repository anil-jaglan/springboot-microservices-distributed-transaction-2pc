package io.learning.account.devil;

public class AccountNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = -1219518716563440469L;

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, Throwable th) {
        super(message, th);
    }
}
