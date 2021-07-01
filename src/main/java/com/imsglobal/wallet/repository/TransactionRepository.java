package com.imsglobal.wallet.repository;

import org.springframework.data.repository.CrudRepository;

import com.imsglobal.wallet.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

}
