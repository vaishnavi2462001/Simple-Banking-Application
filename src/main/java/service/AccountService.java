package service;

import java.math.BigDecimal;

import exception.DuplicateAccountException;
import model.Account;
import repository.AccountRepository;

public class AccountService {
	
	private AccountRepository accountrepo;
	
	public AccountService(AccountRepository accountrepo) {
		this.accountrepo = accountrepo;
	}

	public void addAccount(Account account) throws DuplicateAccountException {
		
		if(accountrepo.findByAccountNumber(account.getAccountNumber())!=null) {
			throw new DuplicateAccountException("Duplicate Account Found");
		}
		accountrepo.save(account);
	
	}
	
	public void deleteAccount(String accountNumber) {
		accountrepo.delete(accountNumber);
	}
	
	public BigDecimal getBalance(String accountNumber) {
	    Account account = accountrepo.findByAccountNumber(accountNumber);
	    if (account == null) {
	        throw new IllegalArgumentException("Account not found with this account number: " + accountNumber);
	    }
	    return account.getBalance();
	}
	
	public Account retrieve(String accountNumber) {
		return accountrepo.findByAccountNumber(accountNumber);
	}

}
