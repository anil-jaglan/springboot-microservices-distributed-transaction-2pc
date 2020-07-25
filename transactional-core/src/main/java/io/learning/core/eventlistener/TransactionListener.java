package io.learning.core.eventlistener;

public interface TransactionListener<T> {

    void handleEvent(T event);

    void handleAfterRollback(T event);

    void handleAfterCompletion(T event);
}
