package com.imsglobal.wallet.dto;

import java.util.HashSet;
import java.util.Set;

public class AccountDto {
	private Long id;
	private String owner;
	private Double balance;
	private Set<String> transactions = new HashSet<>();
	public AccountDto(Long id, String owner, Double balance, Set<String> transactions) {
		this.id = id;
		this.owner = owner;
		this.balance = balance;
		this.transactions = transactions;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Set<String> getTransactions() {
		return transactions;
	}
	public void setTransactions(Set<String> transactions) {
		this.transactions = transactions;
	}
	
	
}
