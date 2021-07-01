package com.imsglobal.wallet.repository;

import org.springframework.data.repository.CrudRepository;

import com.imsglobal.wallet.model.Account;

public interface AccountRepository extends CrudRepository<Account, Long>{

	Account findByOwnerUsername(String username);

}
