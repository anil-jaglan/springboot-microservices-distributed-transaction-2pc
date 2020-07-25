package io.learning.order.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.RestTemplate;

import io.learning.core.domain.DistributedTransactionStatus;
import io.learning.core.eventlistener.TransactionListener;
import io.learning.order.devil.OrderProcessingException;
import io.learning.order.event.OrderTransactionEvent;
import io.learning.order.service.EventBus;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderTransactionListener implements TransactionListener<OrderTransactionEvent> {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EventBus eventBus;

    @Override
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleEvent(OrderTransactionEvent event) throws OrderProcessingException {
        log.debug("Handling event before commit: {}", event);
        eventBus.sendEvent(event);
    }

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleAfterRollback(OrderTransactionEvent event) {
        log.debug("Handling event after rollback : {}", event);
        restTemplate.put(
                "http://transaction-server/transactions/{id}/finish/{status}",
                null,
                event.getTransactionId(),
                DistributedTransactionStatus.ROLLBACK);
    }

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAfterCompletion(OrderTransactionEvent event) {
        log.debug("Handling event after completion : {}", event);
        restTemplate.put(
                "http://transaction-server/transactions/{id}/finish/{status}",
                null,
                event.getTransactionId(),
                DistributedTransactionStatus.CONFIRMED);
    }

}
