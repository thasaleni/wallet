package com.imsglobal.wallet.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;

@Entity
public class Transaction {
	@Id
	@GeneratedValue
	private Long id;
	@CreatedDate
	private LocalDate date;
	private TransactionType transactionType;
	private Double amount;
	@ManyToOne
	@JoinColumn(name = "transaction_id", nullable = false)
	private Account account;
    public Transaction() {}
	public Transaction(Account account,TransactionType transactionType, Double amount) {
		this.account = account;
		this.transactionType = transactionType;
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
