package banking_system.simple_banking_app;

import java.math.BigDecimal;
import exception.*;
import model.Account;
import model.Customer;
import repository.AccountRepository;
import repository.TransactionRepository;
import service.TransactionService;
import service.CustomerService;

public class App {
	public static void main(String[] args) {
		try {
			CustomerService customerService = new CustomerService();
			AccountRepository accountRepository = new AccountRepository();
			TransactionRepository transactionRepository = new TransactionRepository();
			TransactionService transactionService = new TransactionService(accountRepository, transactionRepository);

			Customer customer = customerService.addcustomer("Vaish", "amazon@example.com", "9876504321");

			Account savings = new Account(customer.getCustomerId(), new BigDecimal("100.00"), Account.AccountType.SAVINGS);
			accountRepository.save(savings);

			Account checking = new Account(customer.getCustomerId(), new BigDecimal("500.00"), Account.AccountType.CHECKING);
			accountRepository.save(checking);

			transactionService.deposit(savings.getAccountNumber(), new BigDecimal("200.00"));
			System.out.println("Savings after deposit: " + savings.getBalance());

			transactionService.withdraw(checking.getAccountNumber(), new BigDecimal("50.00"));
			System.out.println("Checking after withdrawal: " + checking.getBalance());

			transactionService.transfer(savings, checking, new BigDecimal("100.00"));
			System.out.println("Savings account after transfer: " + savings.getBalance());
			System.out.println("Checking account after transfer: " + checking.getBalance());

		} catch (AccountNotFoundException | InvalidAmountException | InsufficientFundsException e) {
			System.err.println("Transaction error: " + e.getMessage());
		}
	}
}
