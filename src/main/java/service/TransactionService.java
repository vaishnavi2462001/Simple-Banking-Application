package service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import exception.AccountNotFoundException;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.Transaction;
import model.Transaction.TransactionType;
import repository.AccountRepository;
import repository.TransactionRepository;

public class TransactionService {
	private final AccountRepository accountrepo;
	private final TransactionRepository transrepo;

	public TransactionService(AccountRepository accountrepo, TransactionRepository transrepo) {
		this.accountrepo = accountrepo;
		this.transrepo = transrepo;

	}

	public void deposit(String accountNumber, BigDecimal amount)
			throws AccountNotFoundException, InvalidAmountException {
		Account account = accountrepo.findByAccountNumber(accountNumber);
		if (account == null)
			throw new AccountNotFoundException("Account not Found");
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidAmountException("Invalid Amount ");
		}

		account.setBalance(account.getBalance().add(amount));
		transrepo.save(new Transaction(TransactionType.DEPOSIT, accountNumber, amount, LocalDateTime.now(), account));

	}

	public void withdraw(String accountNumber, BigDecimal amount)
			throws InsufficientFundsException, AccountNotFoundException, InvalidAmountException {
		Account account = accountrepo.findByAccountNumber(accountNumber);
		if (account == null)
			throw new AccountNotFoundException("Account not Found");
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidAmountException("Invalid Amount ");
		}
		BigDecimal balance = account.getBalance();
		if (amount.compareTo(balance) > 0) // amount>balance
			throw new InsufficientFundsException("Insufficient Funds");
		account.setBalance(balance.subtract(amount));
		transrepo
				.save(new Transaction(TransactionType.WITHDRAWAL, accountNumber, amount, LocalDateTime.now(), account));
	}

	public void transfer(Account fromAccount, Account toAccount, BigDecimal amount)
			throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException {
		if (fromAccount == null || toAccount == null) {
			throw new AccountNotFoundException("Source or destination account not found");
		}

		Account fromaccount = accountrepo.findByAccountNumber(fromAccount.getAccountNumber());
		Account toaccount = accountrepo.findByAccountNumber(toAccount.getAccountNumber());

		if (fromaccount == null) {
			throw new AccountNotFoundException("Source account not found");
		}
		if (toaccount == null) {
			throw new AccountNotFoundException("Destination account not found");
		}

		if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
			throw new InvalidAmountException("Cannot transfer to the same account");
		}

		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidAmountException("Invalid amount");
		}

		if (amount.compareTo(fromaccount.getBalance()) > 0) {
			throw new InsufficientFundsException("Insufficient Funds");
		}

		fromaccount.setBalance(fromaccount.getBalance().subtract(amount));
		toaccount.setBalance(toaccount.getBalance().add(amount));

		transrepo.save(new Transaction(TransactionType.TRANSFER, fromaccount.getAccountNumber(), amount.negate(),
				LocalDateTime.now(), toaccount));
		transrepo.save(new Transaction(TransactionType.TRANSFER, toaccount.getAccountNumber(), amount,
				LocalDateTime.now(), fromaccount));
	}
}
