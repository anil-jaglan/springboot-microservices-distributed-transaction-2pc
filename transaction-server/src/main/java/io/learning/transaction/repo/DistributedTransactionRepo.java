package io.learning.transaction.repo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import io.learning.core.domain.DistributedTransaction;

@Repository
public class DistributedTransactionRepo {

    private List<DistributedTransaction> transactions = new ArrayList<>();

    public Optional<DistributedTransaction> findById(String id) {
        return transactions.stream().filter(tx -> tx.getId().equalsIgnoreCase(id)).findAny();
    }

    public List<DistributedTransaction> findAll() {
        return Collections.unmodifiableList(transactions);
    }

    public DistributedTransaction save(DistributedTransaction transaction) {
        transaction.setId(UUID.randomUUID().toString());
        transactions.add(transaction);
        return transaction;
    }

    public DistributedTransaction update(DistributedTransaction transaction) {
        int index = transactions.indexOf(transaction);
        if (index >= 0) {
            transactions.remove(index);
            transactions.add(index, transaction);
        }
        return transaction;
    }

    public boolean deleteById(String id) {
        Optional<DistributedTransaction> txOpt = findById(id);
        if (txOpt.isPresent()) {
            transactions.remove(txOpt.get());
            return true;
        }
        return false;
    }

}
