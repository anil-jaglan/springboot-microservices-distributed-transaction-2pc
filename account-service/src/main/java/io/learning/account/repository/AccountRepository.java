package io.learning.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.learning.account.domain.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByCustomerId(Long customerId);

}
