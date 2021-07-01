package com.imsglobal.wallet.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.imsglobal.wallet.dto.AccountDto;
import com.imsglobal.wallet.service.WalletService;

@Controller
@RestController
@RequestMapping("/api/wallet")
public class WalletController {
	private final WalletService walletService;

	@Autowired
	public WalletController(WalletService walletService) {
		this.walletService = walletService;
	}
	
	@GetMapping(value = "/{username}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ROLE_USER')")
	public AccountDto getWalletForUser(@PathVariable String username) {
		return walletService.getAccountForUser(username);
	}
	
	@PostMapping(value = {"transact/{transactionType}/{amount}/{fromAccountUsername}","/transact/{transactionType}/{amount}/{fromAccountUsername}/{toAccountUsername}"})
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<? extends Object> transact(@PathVariable Double amount, @PathVariable String transactionType, @PathVariable String fromAccountUsername, @PathVariable(required = false) String toAccountUsername) {
		AccountDto wallet = null;
		try {
			 wallet = walletService.transact(fromAccountUsername,toAccountUsername,  amount, transactionType);
		}catch(IllegalArgumentException e) {
			return  new ResponseEntity<String> (e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<AccountDto> (wallet, HttpStatus.OK);
	}
    
}
