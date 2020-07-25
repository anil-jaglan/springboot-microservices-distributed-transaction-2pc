package io.learning.account.event;

import io.learning.account.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountTransactionEvent {

    private String transactionId;

    private Account account;

}
