package com.micro.accounts.repository;

import com.micro.accounts.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {
    Account findByCustomerId(int customerId);
}
