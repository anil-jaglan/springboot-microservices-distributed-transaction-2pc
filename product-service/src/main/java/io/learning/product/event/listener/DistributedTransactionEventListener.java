package io.learning.product.event.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.learning.core.domain.DistributedTransaction;
import io.learning.product.service.EventBus;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DistributedTransactionEventListener {

    @Autowired
    private EventBus eventBus;

    @RabbitListener(bindings = { 
                @QueueBinding(value = @Queue("txn-events-product"), exchange = @Exchange(type = ExchangeTypes.TOPIC, name = "txn-events")) 
    })
    public void onMessage(DistributedTransaction transaction) {
        log.debug("Transaction message received: {}", transaction);
        eventBus.sendTransaction(transaction);
    }

}
