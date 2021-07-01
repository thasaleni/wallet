package com.imsglobal.wallet.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.imsglobal.wallet.dto.AccountDto;
import com.imsglobal.wallet.model.Account;
import com.imsglobal.wallet.model.Transaction;
import com.imsglobal.wallet.model.TransactionType;
import com.imsglobal.wallet.repository.AccountRepository;
import com.imsglobal.wallet.repository.TransactionRepository;

@Service
public class WalletService {
	private static AccountRepository accountRepository;
	private static TransactionRepository transactionRepository;

	@Autowired
	public WalletService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public AccountDto getAccountForUser(String username) {
		Account account = accountRepository.findByOwnerUsername(username);
		if(account == null) {
			throw new IllegalArgumentException("Account not found.");
		}
		return toDto(account);
	}
	public Account createAccount(Account account) {
//		if(account.getOwner() == null) {
//			throw new IllegalArgumentException("Account must have an owner.");
//		}
		return accountRepository.save(account);
	}
	public AccountDto transact(String fromAccountUsername, String toAccountUsername, Double amount, String transactionType) {
		Account wallet = accountRepository.findByOwnerUsername(fromAccountUsername);
		Account toWallet = null;
		Transaction transaction =null;
		Transaction transferToTransaction = null;
		if (toAccountUsername != null) {
			toWallet = accountRepository.findByOwnerUsername(toAccountUsername);
		}
		if (wallet!= null) {
			switch (TransactionType.valueOf(transactionType)) {
			case DEPOSIT:
				wallet.setBalance(wallet.getBalance() + amount);
				transaction = new Transaction(wallet, TransactionType.valueOf(transactionType), amount);
				break;
			case WITHDRAW:
				if ((wallet.getBalance() - amount) < 0) {
					throw new IllegalArgumentException("You have insufficient funds for withdrawal amount.");
				} else {
					wallet.setBalance(wallet.getBalance() - amount);
				}
				transaction = new Transaction(wallet, TransactionType.valueOf(transactionType), -amount);
				break;
			case TRANSFER:
				if (toWallet == null) {
					throw new IllegalArgumentException("Account for user "+toAccountUsername+" does not exist.");
				}
				if ((wallet.getBalance() - amount) < 0) {
					throw new IllegalArgumentException("You have insufficient funds for transfer amount.");
				} else {
					wallet.setBalance(wallet.getBalance() - amount);
					toWallet.setBalance(toWallet.getBalance() + amount);
				}
				transaction = new Transaction(wallet, TransactionType.valueOf(transactionType), -amount);
				transferToTransaction = new Transaction(toWallet, TransactionType.valueOf(transactionType), amount);
				break;
			}
			transactionRepository.save(transaction);
			
			accountRepository.save(wallet);
			if (toWallet != null) {
				transactionRepository.save(transferToTransaction);
				accountRepository.save(toWallet);
			}
			return toDto(wallet);
		} else {
			throw new IllegalArgumentException(
					"Account not found for user: " + wallet != null ? toAccountUsername : fromAccountUsername);
		}
	}

	private AccountDto toDto(Account account) {
		return new AccountDto(account.getId(), account.getOwner().getUsername(), account.getBalance(),
				account.getTransactions().stream()
						.map(t -> t.getId() + "  " + t.getTransactionType() + "  " + t.getAmount() + "  " + t.getDate())
						.collect(Collectors.toSet()));
	}

	private Account fromDto(AccountDto dto) {
		return accountRepository.findById(dto.getId()).get();
	}

	
}
