package io.learning.order.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import io.learning.core.domain.Account;
import io.learning.core.domain.DistributedTransaction;
import io.learning.core.domain.DistributedTransactionParticipant;
import io.learning.core.domain.DistributedTransactionStatus;
import io.learning.core.domain.Product;
import io.learning.order.devil.InSufficientFundException;
import io.learning.order.devil.OrderProcessingException;
import io.learning.order.domain.Order;
import io.learning.order.event.OrderTransactionEvent;
import io.learning.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

    private static final String TXN_ID_HEADER = "X-Txn-ID";

    private final Random random = new Random();

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Transactional
    public Order createOrder(Order order) {
        DistributedTransaction transaction = restTemplate.postForObject("http://transaction-server/transactions", new DistributedTransaction(), DistributedTransaction.class);
        log.info("Trasaction created: {}", transaction);
        Order savedOrder = orderRepository.save(order);
        Product product = updateProduct(transaction.getId(), savedOrder);
        log.info("Product updated: {}", product);
        int totalAmount = product.getPrice() * order.getQuantity();
        Account account = restTemplate.getForObject("http://account-service/accounts/customer/{customerId}", Account.class, order.getCustomerId());
        log.info("Account :{}", account);
        if (account.getBalance() >= totalAmount) {
            log.info("Withdrawing money: {}", totalAmount);
            withdraw(transaction.getId(), account.getId(), totalAmount);
        } else {
            throw new InSufficientFundException("Insufficient funds. Balance: " + account.getBalance() + ", orderAmount:  " + totalAmount);
        }
        eventPublisher.publishEvent(new OrderTransactionEvent(transaction.getId()));
        int number = random.nextInt();
        if (number % 2 == 0) {
            throw new OrderProcessingException("Error while processing your order");
        }
        return savedOrder;
    }

    protected Product updateProduct(String transactionId, Order order) {
        addTransactionParticipant(transactionId, "product-service", DistributedTransactionStatus.NEW);
        HttpEntity<Void> requestEntity = new HttpEntity<>(prepareHeaders(transactionId));
        return restTemplate.exchange(
                "http://product-service/products/{id}/quantity/{quantity}",
                HttpMethod.PUT,
                requestEntity,
                Product.class,
                order.getProductId(),
                order.getQuantity()).getBody();
    }

    protected Account withdraw(String transactionId, Long accountId, int amount) {
        addTransactionParticipant(transactionId, "account-service", DistributedTransactionStatus.NEW);
        HttpEntity<Void> requestEntity = new HttpEntity<>(prepareHeaders(transactionId));
        return restTemplate.exchange("http://account-service/accounts/{id}/withdrawl/{amount}", HttpMethod.PUT, requestEntity, Account.class, accountId, amount).getBody();
    }
    
    protected void addTransactionParticipant(String transactionId, String serviceId, DistributedTransactionStatus status) {
        HttpEntity<DistributedTransactionParticipant> requestEntity = new HttpEntity<>(new DistributedTransactionParticipant(serviceId, status));
        restTemplate.exchange("http://transaction-server/transactions/{id}/participants", HttpMethod.PUT, requestEntity, Object.class, transactionId);
    }

    private HttpHeaders prepareHeaders(String transactionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(TXN_ID_HEADER, transactionId);
        return headers;
    }

}
