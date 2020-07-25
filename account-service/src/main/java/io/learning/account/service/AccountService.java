package io.learning.account.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.learning.account.devil.AccountNotFoundException;
import io.learning.account.domain.Account;
import io.learning.account.event.AccountTransactionEvent;
import io.learning.account.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account findByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId).orElseThrow(() -> new AccountNotFoundException("Account not exists for customer ID: " + customerId));
    }

    @Async
    @Transactional
    public void deposit(Long accountId, int amount, String transactionId) {
        transfer(accountId, amount, transactionId);
    }

    @Async
    @Transactional
    public void withdrawl(Long accountId, int amount, String transactionId) {
        transfer(accountId, amount * (-1), transactionId);
    }

    protected void transfer(Long accountId, int amount, String transactionId) {
        log.info("Transferring money to account={}, amount={}, txnId: {}", accountId, amount, transactionId);
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        accountOpt.ifPresent(account -> {
            account.setBalance(account.getBalance() + amount);
            eventPublisher.publishEvent(new AccountTransactionEvent(transactionId, account));
            accountRepository.save(account);
        });
    }
}
